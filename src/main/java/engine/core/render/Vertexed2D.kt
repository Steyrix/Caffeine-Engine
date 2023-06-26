package engine.core.render

import engine.core.render.opengl_wrapper.buffer.Buffer
import engine.core.render.opengl_wrapper.VertexArray

open class Vertexed2D(
        dataArrays: List<FloatArray>,
        protected val verticesCount: Int,
) {

    protected val buffers: MutableList<Buffer> = mutableListOf()
    protected val vertexArray = VertexArray()

    init {
        initBuffers(dataArrays)
        initVertexArray()
    }

    private fun initBuffers(
            dataArrays: List<FloatArray>
    ) {
        dataArrays.forEach {
            buffers.add(
                    Buffer(it, verticesCount)
            )
        }
    }

    private fun initVertexArray() {
        buffers.forEachIndexed { index, it ->
            vertexArray.addBuffer(it, index)
        }
    }

    fun updateBuffer(
            bufferIndex: Int,
            offset: Long,
            data: FloatArray
    ) {
        buffers[bufferIndex].update(data, offset)
    }
}