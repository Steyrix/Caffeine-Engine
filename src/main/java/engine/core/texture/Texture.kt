package engine.core.texture

import org.lwjgl.opengl.GL33C.*

interface Texture {
    val id: Int

    fun bind()

    fun unbind()

    fun setParameter(name: Int, value: Int) {
        bind()
        glTexParameteri(GL_TEXTURE_2D, name, value)
        unbind()
    }
}