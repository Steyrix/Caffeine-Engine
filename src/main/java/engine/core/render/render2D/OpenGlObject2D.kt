package engine.core.render.render2D

import engine.core.render.Drawable2D
import engine.core.shader.Shader
import engine.core.texture.Texture2D
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.matrix.MatrixComputer
import org.lwjgl.opengl.GL33C.*
import java.nio.IntBuffer

//TODO: Create Drawable2D class to encapsulate vertex buffer and vertex array logic
open class OpenGlObject2D(
        bufferParamsCount: Int,
        dataArrays: List<FloatArray>,
        verticesCount: Int,
        private val texture: Texture2D?
): Vertexed2D(bufferParamsCount, dataArrays, verticesCount), Drawable2D {

    override var shader: Shader? = null
    override var x: Float = 0f
    override var y: Float = 0f
    override var xSize: Float = 0f
    override var ySize: Float = 0f
    override var rotationAngle: Float = 0f

    var boundingBox: BoundingBox? = null
    private val boundingBoxBuffer: IntBuffer = IntBuffer.allocate(1)
    private val boundingBoxVertexArray = IntBuffer.allocate(1)
    private val boundingBoxVerticesCount = 8

    override fun draw2D() {
        shader?.let {
            it.bind()

            val model = MatrixComputer.getResultMatrix(x, y, xSize, ySize, rotationAngle)

            // TODO: Define texture state
            it.setUniform(Shader.VAR_KEY_MODEL, model)

            glBindVertexArray(vertexArray.get(0))
            glDrawArrays(GL_TRIANGLES, 0, verticesCount)
        }
    }

    fun dispose() {
        glDeleteBuffers(buffers)
        glDeleteVertexArrays(vertexArray)
    }
}