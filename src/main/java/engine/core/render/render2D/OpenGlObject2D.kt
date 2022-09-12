package engine.core.render.render2D

import engine.core.entity.Entity
import engine.core.shader.Shader
import engine.core.texture.ArrayTexture2D
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParameters
import engine.core.update.SetOfParameters
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.matrix.MatrixComputer
import org.lwjgl.opengl.GL33C.*

open class OpenGlObject2D(
        bufferParamsCount: Int,
        dataArrays: List<FloatArray>,
        verticesCount: Int,
        var texture: Texture2D? = null,
        var arrayTexture: ArrayTexture2D? = null
): Vertexed2D(bufferParamsCount, dataArrays, verticesCount), Drawable2D, Entity {

    override var shader: Shader? = null

    var x: Float = 0f
    var y: Float = 0f
    var xSize: Float = 0f
    var ySize: Float = 0f
    var rotationAngle: Float = 0f

    override val innerDrawableComponents: MutableList<Drawable2D> = mutableListOf()

    var boundingBox: BoundingBox? = null
    set(value) {
        if (value != null) innerDrawableComponents.add(value)
        field = value
    }

    private var textureUniformName: String = ""

    override fun draw() {
        shader?.let {
            it.bind()

            val model = MatrixComputer.getResultMatrix(x, y, xSize, ySize, rotationAngle)

            defineTextureState()
            it.setUniform(Shader.VAR_KEY_MODEL, model)

            it.validate()

            glBindVertexArray(vertexArrayHandle)
            glDrawArrays(GL_TRIANGLES, 0, verticesCount)
        }
        super.draw()
    }

    override fun updateParameters(parameters: SetOfParameters) {
        innerDrawableComponents.forEach {
            it.updateParameters(parameters)
        }
        if (parameters is SetOf2DParameters) {
            parameters.let {
                x = it.x
                y = it.y
                xSize = it.xSize
                ySize = it.ySize
                rotationAngle = it.rotationAngle
            }
        }
    }

    fun dispose() {
        bufferHandles.forEach {
            glDeleteBuffers(it)
        }
        glDeleteVertexArrays(vertexArrayHandle)
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
        glActiveTexture(GL_TEXTURE0)
        arrayTexture!!.bind()
        shader!!.bind()
        shader!!.setUniform(textureUniformName, 0)
    }

    fun isTextured(): Boolean {
        return texture != null || arrayTexture != null
    }
}