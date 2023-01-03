package engine.core.texture

import org.lwjgl.opengl.GL33C.*

// TODO use specific import instead of wildcard
interface Texture {
    val id: Int
    val bindTarget: Int

    fun bind() {
        glBindTexture(bindTarget, id)
    }

    fun unbind() {
        glBindTexture(bindTarget, 0)
    }

    fun setParameter(name: Int, value: Int) {
        bind()
        glTexParameteri(bindTarget, name, value)
        unbind()
    }
}