package engine.core.render.render2D

import org.lwjgl.opengl.GL30.*

class VertexArray(

) {

    private var handle: Int = 0

    init {
        handle = glGenVertexArrays()
    }

    fun addBuffer(bufferId: Int, index: Int) {
        bind()

        glBindBuffer(GL_ARRAY_BUFFER, bufferId)
    }

    fun bind() {
        glBindVertexArray(handle)
    }

    fun unbind() {
        glBindVertexArray(0)
    }
}