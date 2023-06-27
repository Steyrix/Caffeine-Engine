package engine.core.render

import engine.core.render.opengl_wrapper.buffer.Buffer
import engine.core.render.opengl_wrapper.VertexArray

open class Mesh(
        dataArrays: List<FloatArray>,
        val verticesCount: Int,
) {

    private val buffers: MutableList<Buffer> = mutableListOf()
    private val vertexArray = VertexArray()

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

    fun prepare() {
        vertexArray.bind()
    }

    fun dispose() {
        vertexArray.dispose()
        buffers.forEach { it.dispose() }
    }
}