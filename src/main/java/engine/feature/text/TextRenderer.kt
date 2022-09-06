package engine.feature.text

import com.sun.media.sound.InvalidFormatException
import engine.core.render.render2D.OpenGlObject2D
import engine.core.shader.Shader
import engine.core.texture.Texture2D
import engine.feature.geometry.Point2D
import engine.feature.util.Buffer
import java.awt.Dimension

class TextRenderer(
        private val textureAtlas: Texture2D?,
        private val characterCoordinates: HashMap<Char, Point2D>?,
        private val charSize: Dimension,
        private var textShader: Shader
) {
    companion object {
        private const val ATLAS_WRONG_FORMAT_ERROR_TEXT = "Invalid texture atlas format"
        private const val NULL_COORDINATES_MAP_ERROR_TEXT = "Character coordinates map is null"
        private const val NULL_ATLAS_ERROR_TEXT = "Texture atlas is null"
        // TODO: error text to constants, remove exception handling
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

        private fun generateMap(charSize: Dimension,
                                textureAtlas: Texture2D,
                                characters: MutableList<Char>): HashMap<Char, Point2D> {
            val out = HashMap<Char, Point2D>()
            val xStep: Int
            val yStep: Int
            val charsCount: Int

            val widthIsValid = textureAtlas.getWidthF() % charSize.getWidth() == 0.0
            val heightIsValid = textureAtlas.getHeightF() % charSize.getHeight() == 0.0

            if (widthIsValid && heightIsValid) {
                xStep = (textureAtlas.getWidthF() / charSize.getWidth()).toInt()
                yStep = (textureAtlas.getHeightF() / charSize.getHeight()).toInt()
                charsCount = xStep * yStep
                if (charsCount != characters.size)
                    throw InvalidFormatException(ATLAS_WRONG_FORMAT_ERROR_TEXT)
            } else
                throw InvalidFormatException(ATLAS_WRONG_FORMAT_ERROR_TEXT)

            var j = 0
            var k = 0

            for (i in 0 until charsCount) {
                if (charSize.getWidth() * j >= textureAtlas.getWidthF()) {
                    j = 0
                    k++
                }
                out[characters[i]] = Point2D(j++.toFloat(), k.toFloat())
            }

            return out
        }
    }

    private val cache: HashMap<Char, OpenGlObject2D> = HashMap()

    val isValid: Boolean
        get() = this.textureAtlas != null && this.characterCoordinates != null

    override fun toString(): String =
            "Text renderer with " + characterCoordinates!!.size + " characters. \n" + characterCoordinates.toString()

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

            val horizontalShift = (fontSize.getWidth() * x++).toFloat()
            val verticalShift = (fontSize.getHeight() * y).toFloat()

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
                width * curr.x,
                height * curr.y,
                width * (curr.x + 1),
                height * (curr.y + 1),
                width * curr.x,
                height * (curr.y + 1),
                width * curr.x,
                height * curr.y,
                width * (curr.x + 1),
                height * curr.y,
                width * (curr.x + 1),
                height * (curr.y + 1)
        )
    }

    fun setShader(shader: Shader) {
        textShader = shader
    }

    private fun drawCharacter(
            char: Char,
            fontSize: Dimension,
            pos: Point2D
    ) {
        val glObject: OpenGlObject2D

        if (!cache.containsKey(char)) {
            val uvCoordinates = getCharUV(char)
            val bufferData = Buffer.RECTANGLE_INDICES
            glObject = OpenGlObject2D(
                    bufferParamsCount = 2,
                    dataArrays = listOf(uvCoordinates, bufferData),
                    verticesCount = 6,
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
            glObject = cache[char]!!
        }

        glObject.draw()
    }
}