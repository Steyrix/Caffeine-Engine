package engine.feature.text

import engine.core.render.Mesh
import engine.core.render.Model
import engine.core.shader.Shader
import engine.core.texture.Texture2D
import engine.feature.geometry.Point2D
import engine.feature.text.TextRendererUtil.generateMap
import engine.core.render.util.DefaultBufferData
import java.awt.Dimension

// TODO: ability to reduce gaps between letters and make it look natural
class TextRenderer(
        private val textureAtlas: Texture2D?,
        private val characterCoordinates: HashMap<Char, Point2D>?,
        private val charSize: Dimension,
        private var textShader: Shader
) {
    companion object {
        private const val NULL_COORDINATES_MAP_ERROR_TEXT = "Character coordinates map is null"
        private const val NULL_ATLAS_ERROR_TEXT = "Texture atlas is null"

        fun getInstance(
                charSize: Dimension,
                textureFilePath: String,
                characters: MutableList<Char>,
                initialShader: Shader
        ): TextRenderer {
            val textureAtlas: Texture2D = Texture2D.createInstance(textureFilePath)
            val characterCoordinates = generateMap(charSize, textureAtlas, characters)

            return TextRenderer(textureAtlas!!, characterCoordinates!!, charSize, initialShader)
        }
    }

    private val cache: HashMap<Char, Model> = HashMap()

    val isValid: Boolean
        get() = this.textureAtlas != null && this.characterCoordinates != null

    override fun toString(): String =
            "Text renderer with " + characterCoordinates!!.size + " characters. \n" + characterCoordinates.toString()

    fun setShader(shader: Shader) {
        textShader = shader
    }

    // TODO: implement modifiable horizontal and vertical gaps
    fun drawText(
            text: String,
            fontSize: Dimension,
            pos: Point2D
    ) {
        var x = 0
        var y = 0
        for (c in text.toCharArray()) {
            if (c == '\n') {
                y++
                x = 0
                continue
            }

            val horizontalShift = (fontSize.getWidth() / 1.5 * x++).toFloat()
            val verticalShift = (fontSize.getHeight() / 2 * y).toFloat()

            val horizontalPos = pos.x + horizontalShift
            val verticalPos = pos.y + verticalShift

            drawCharacter(
                    char = c,
                    fontSize = fontSize,
                    pos = Point2D(horizontalPos, verticalPos)
            )
        }
    }

    private fun getCharUV(c: Char): FloatArray {
        val curr = if (characterCoordinates != null) {
            characterCoordinates[c] ?: throw Exception("character $c coordinates not found in map")
        } else {
            throw NullPointerException(NULL_COORDINATES_MAP_ERROR_TEXT)
        }

        if (textureAtlas == null) {
            throw NullPointerException(NULL_ATLAS_ERROR_TEXT)
        }

        val width = (charSize.getWidth() / textureAtlas.getWidthF()).toFloat()
        val height = (charSize.getHeight() / textureAtlas.getHeightF()).toFloat()

        return floatArrayOf(
                width * curr.x,       height * curr.y,
                width * (curr.x + 1), height * (curr.y + 1),
                width * curr.x,       height * (curr.y + 1),
                width * curr.x,       height * curr.y,
                width * (curr.x + 1), height * curr.y,
                width * (curr.x + 1), height * (curr.y + 1)
        )
    }

    private fun drawCharacter(
            char: Char,
            fontSize: Dimension,
            pos: Point2D
    ) {
        val glObject: Model

        if (!cache.containsKey(char)) {
            val uvCoordinates = getCharUV(char)
            val bufferData = DefaultBufferData.RECTANGLE_INDICES
            glObject = Model(
                    mesh = Mesh(
                            dataArrays = listOf(bufferData, uvCoordinates),
                            verticesCount = 6
                    ),
                    texture = textureAtlas
            ).apply {
                x = pos.x
                y = pos.y
                xSize = fontSize.getWidth().toFloat()
                ySize = fontSize.getHeight().toFloat()
                rotationAngle = 0f
                shader = textShader
            }

            cache[char] = glObject
        } else {
            glObject = cache[char]!!.apply {
                x = pos.x
                y = pos.y
                xSize = fontSize.getWidth().toFloat()
                ySize = fontSize.getHeight().toFloat()
            }
        }

        glObject.draw()
    }
}