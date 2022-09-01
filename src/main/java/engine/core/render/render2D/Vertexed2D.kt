package engine.core.render.render2D

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33C.*

open class Vertexed2D(
        bufferParamsCount: Int,
        dataArrays: List<FloatArray>,
        protected val verticesCount: Int,
) {

    private var buffersFilled: Int = 0
    protected val bufferHandles: MutableList<Int> = mutableListOf()
    protected var vertexArrayHandle: Int = 0

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

        dataArrays.forEach {
            val bufferHandle = glGenBuffers()
            bufferHandles.add(bufferHandle)

            val floatBuffer = BufferUtils.createFloatBuffer(2 * it.size)
            floatBuffer.put(it)

            glBindBuffer(GL_ARRAY_BUFFER, bufferHandles[buffersFilled++])
            glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW)

            paramsCount.add(it.size / verticesCount)
        }
    }

    private fun initVertexArray() {
        vertexArrayHandle = glGenVertexArrays()
        glBindVertexArray(vertexArrayHandle)

        for (attribIndex in 0 until buffersFilled) {
            glEnableVertexAttribArray(attribIndex)
            glBindBuffer(GL_ARRAY_BUFFER, bufferHandles[attribIndex])
            glVertexAttribPointer(attribIndex, paramsCount[attribIndex], GL_FLOAT, false, 0, 0)
        }
    }
}