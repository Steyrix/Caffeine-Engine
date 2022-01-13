package engine.core.render2D

import engine.core.texture.Texture2D
import org.lwjgl.opengl.GL33C.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

open class OpenGlObject2D(
        bufferParamsCount: Int,
        dataArrays: List<FloatArray>,
        private val verticesCount: Int,
        private val texture: Texture2D?
) {

    private var buffersFilled: Int = 0
    private val buffers = IntBuffer.allocate(bufferParamsCount)
    private val vertexArray = IntBuffer.allocate(1)

    private val boxBuffer: IntBuffer = IntBuffer.allocate(1)
    private val boxVertexArray = IntBuffer.allocate(1)

    private val boxVerticesCount = 8

    private val paramsCount = mutableListOf<Int>()

    init {
        initBuffers(dataArrays, bufferParamsCount)
        initVertexArray()
    }

    private fun initBuffers(
        dataArrays: List<FloatArray>,
        bufferParamsCount: Int
    ) {
        require(dataArrays.size == bufferParamsCount)
        glGenBuffers(IntBuffer.allocate(1))

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

    fun draw() {

    }

    fun dispose() {
        glDeleteBuffers(buffers)
        glDeleteVertexArrays(vertexArray)
    }
}