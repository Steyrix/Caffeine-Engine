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
            initFloatBuffer(it)
        }
    }

    private fun initFloatBuffer(
            dataArray: FloatArray
    ) {
        val bufferHandle = glGenBuffers()
        bufferHandles.add(bufferHandle)

        val floatBuffer = BufferUtils.createFloatBuffer(2 * dataArray.size)
        floatBuffer.put(dataArray)
        floatBuffer.flip()

        glBindBuffer(GL_ARRAY_BUFFER, bufferHandles[buffersFilled++])
        glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW)

        paramsCount.add(dataArray.size / verticesCount)
    }

    private fun initVertexFloatAttribute(
            location: Int
    ) {
        glEnableVertexAttribArray(location)
        glBindBuffer(GL_ARRAY_BUFFER, bufferHandles[location])
        glVertexAttribPointer(location, paramsCount[location], GL_FLOAT, false, 0, 0)
    }

    private fun initVertexArray() {
        vertexArrayHandle = glGenVertexArrays()
        glBindVertexArray(vertexArrayHandle)

        for (attribIndex in 0 until buffersFilled) {
            initVertexFloatAttribute(attribIndex)
        }
    }

    fun updateBuffer(
            bufferIndex: Int,
            offset: Long,
            data: FloatArray
    ) {
        glBindBuffer(GL_ARRAY_BUFFER, bufferHandles[bufferIndex])

        val floatBuffer = BufferUtils.createFloatBuffer(2 * data.size)
        floatBuffer.put(data)
        floatBuffer.flip()

        glBufferSubData(GL_ARRAY_BUFFER, offset, floatBuffer)
    }
}