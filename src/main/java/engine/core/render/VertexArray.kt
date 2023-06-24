package engine.core.render

import engine.core.render.buffer.Buffer
import org.lwjgl.opengl.GL30.*

class VertexArray {

    private var handle: Int = 0

    init {
        handle = glGenVertexArrays()
    }

    fun addBuffer(buffer: Buffer, index: Int) {
        bind()
        glEnableVertexAttribArray(index)

        buffer.bind()
        glVertexAttribPointer(index, buffer.componentCount, GL_FLOAT, false, 0, 0)
        buffer.unbind()

        unbind()
    }

    fun bind() {
        glBindVertexArray(handle)
    }

    fun unbind() {
        glBindVertexArray(0)
    }

    fun dispose() {
        glDeleteVertexArrays(handle)
    }
}