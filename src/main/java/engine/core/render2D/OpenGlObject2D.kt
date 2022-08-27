package engine.core.render2D

import engine.core.shader.Shader
import engine.core.texture.Texture2D
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.matrix.MatrixComputer
import org.lwjgl.opengl.GL33C.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

//TODO: Create Drawable2D class to encapsulate vertex buffer and vertex array logic
open class OpenGlObject2D(
        bufferParamsCount: Int,
        dataArrays: List<FloatArray>,
        private val verticesCount: Int,
        private val texture: Texture2D?
) {

    private var buffersFilled: Int = 0
    private val buffers = IntBuffer.allocate(bufferParamsCount)
    private val vertexArray = IntBuffer.allocate(1)

    private val paramsCount = mutableListOf<Int>()

    var boundingBox: BoundingBox? = null
    private val boundingBoxBuffer: IntBuffer = IntBuffer.allocate(1)
    private val boundingBoxVertexArray = IntBuffer.allocate(1)
    private val boundingBoxVerticesCount = 8

    init {
        initBuffers(dataArrays, bufferParamsCount)
        initVertexArray()
    }

    private fun initBuffers(
        dataArrays: List<FloatArray>,
        bufferParamsCount: Int
    ) {
        require(dataArrays.size == bufferParamsCount)
        glGenBuffers(IntBuffer.allocate(buffers.capacity()))

        dataArrays.forEach {
            val floatBuffer = FloatBuffer.wrap(it)

            glBindBuffer(GL_ARRAY_BUFFER, buffers.get(buffersFilled++))
            glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW)

            paramsCount.add(it.size / verticesCount)
        }
    }

    private fun initVertexArray() {
        glGenVertexArrays(vertexArray)
        glBindVertexArray(vertexArray.get(0))

        for (attribIndex in 0 until buffersFilled) {
            glEnableVertexAttribArray(attribIndex)
            glBindBuffer(GL_ARRAY_BUFFER, buffers.get(attribIndex))
            glVertexAttribPointer(attribIndex, paramsCount[attribIndex], GL_FLOAT, false, 0 , 0)
        }
    }

    fun draw(x: Float, y: Float, xSize: Float, ySize: Float, rotationAngle: Float, shader: Shader) {
        shader.bind()

        val model = MatrixComputer.getResultMatrix(x, y, xSize, ySize, rotationAngle)

        // TODO: Define texture state

        shader.setUniform(Shader.VAR_KEY_MODEL, model)

        glBindVertexArray(vertexArray.get(0))
        glDrawArrays(GL_TRIANGLES, 0, verticesCount)
    }

    fun dispose() {
        glDeleteBuffers(buffers)
        glDeleteVertexArrays(vertexArray)
    }
}