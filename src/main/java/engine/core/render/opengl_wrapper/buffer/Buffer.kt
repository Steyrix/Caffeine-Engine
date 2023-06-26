package engine.core.render.opengl_wrapper.buffer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL15.*
import java.nio.FloatBuffer

private const val FLOAT_BYTE_SIZE = 2

class Buffer(
        data: FloatArray,
        vertexCount: Int
) {

    private val handle: Int = glGenBuffers()

    val componentCount: Int = data.size / vertexCount

    init {
        val floatBuffer = prepareData(data)
        bind()

        glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW)
    }

    fun update(data: FloatArray, offset: Long) {
        val floatBuffer = prepareData(data)
        bind()

        glBufferSubData(GL_ARRAY_BUFFER, offset, floatBuffer)
    }

    fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, handle)
    }

    fun unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    fun dispose() {
        glDeleteBuffers(handle)
    }

    private fun prepareData(data: FloatArray): FloatBuffer {
        return BufferUtils.createFloatBuffer(FLOAT_BYTE_SIZE * data.size)
                .apply {
                    put(data)
                    flip()
                }
    }
}