package engine.core.render.render2D

import org.lwjgl.opengl.GL33C.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

open class Vertexed2D(
        bufferParamsCount: Int,
        dataArrays: List<FloatArray>,
        protected val verticesCount: Int,
) {

    private var buffersFilled: Int = 0
    protected val buffers: IntBuffer = IntBuffer.allocate(bufferParamsCount)
    protected val vertexArray: IntBuffer = IntBuffer.allocate(1)

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
            glVertexAttribPointer(attribIndex, paramsCount[attribIndex], GL_FLOAT, false, 0, 0)
        }
    }
}