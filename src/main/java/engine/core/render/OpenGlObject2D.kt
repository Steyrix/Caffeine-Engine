package engine.core.render

import engine.core.entity.Entity
import engine.core.shader.Shader
import engine.core.texture.ArrayTexture2D
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import engine.core.update.SetOfParameters
import engine.feature.matrix.MatrixComputer
import engine.feature.util.DefaultBufferData
import org.lwjgl.opengl.GL33C.*

open class OpenGlObject2D(
        bufferParamsCount: Int,
        dataArrays: List<FloatArray>,
        verticesCount: Int,
        var texture: Texture2D? = null,
        var arrayTexture: ArrayTexture2D? = null
): Vertexed2D(bufferParamsCount, dataArrays, verticesCount), Drawable2D, Entity {

    constructor(
            texture2D: Texture2D,
            uv: FloatArray = DefaultBufferData.getRectangleSectorVertices(1.0f, 1.0f)
    ) : this(
            bufferParamsCount = 2,
            dataArrays = listOf(DefaultBufferData.RECTANGLE_INDICES, uv),
            verticesCount = 6,
            texture = texture2D,
            arrayTexture = null
    )

    constructor(arrayTexture2D: ArrayTexture2D) : this(
            bufferParamsCount = 2,
            dataArrays = listOf(DefaultBufferData.RECTANGLE_INDICES, DefaultBufferData.getRectangleSectorVertices(1.0f, 1.0f)),
            verticesCount = 6,
            texture = null,
            arrayTexture = arrayTexture2D
    )

    override var shader: Shader? = null

    var x: Float = 0f
    var y: Float = 0f
    var xSize: Float = 0f
    var ySize: Float = 0f
    var rotationAngle: Float = 0f

    override val innerDrawableComponents: MutableList<Drawable2D> = mutableListOf()

    private var textureUniformName: String = ""

    override fun draw() {
        shader?.let {
            vertexArray.bind()
            it.bind()

            val model = MatrixComputer.getResultMatrix(x, y, xSize, ySize, rotationAngle)

            defineTextureState()
            it.setUniform(Shader.VAR_KEY_MODEL, model)
            it.validate()

            glDrawArrays(GL_TRIANGLES, 0, verticesCount)
        }
        super.draw()
    }

    override fun updateParameters(parameters: SetOfParameters) {
        var mutableParams = parameters

        if (parameters is SetOf2DParametersWithVelocity) {
            mutableParams = SetOfStatic2DParameters(
                    parameters.x,
                    parameters.y,
                    parameters.xSize,
                    parameters.ySize,
                    parameters.rotationAngle
            )
        }

        innerDrawableComponents.forEach {
            it.updateParameters(mutableParams)
        }

        if (mutableParams is SetOfStatic2DParameters) {
            mutableParams.let {
                x = it.x
                y = it.y
                xSize = it.xSize
                ySize = it.ySize
                rotationAngle = it.rotationAngle
            }
        }
    }

    fun dispose() {
        buffers.forEach {
            it.dispose()
        }
        vertexArray.dispose()
    }

    private fun defineTextureState() {
        if (texture != null) {
            textureUniformName = Shader.VAR_KEY_TEXTURE_SAMPLE
            bindTexture()
        } else if (arrayTexture != null) {
            textureUniformName = Shader.VAR_KEY_TEXTURE_ARRAY
            bindArrayTexture()
        }
    }

    private fun bindTexture() {
        glActiveTexture(GL_TEXTURE0)
        texture!!.bind()
        shader!!.bind()
        shader!!.setUniform(textureUniformName, 0)
    }

    private fun bindArrayTexture() {
        // glActiveTexture(GL_TEXTURE0)
        arrayTexture!!.bind()
        shader!!.bind()
        shader!!.setUniform(textureUniformName, 0)
    }

    fun isTextured(): Boolean {
        return texture != null || arrayTexture != null
    }
}