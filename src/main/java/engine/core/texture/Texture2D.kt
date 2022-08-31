package engine.core.texture

import org.lwjgl.opengl.GL33C.*

class Texture2D(
        override val id: Int
) : Texture {

    companion object {
        fun createInstance(src: String): Texture2D {
            return Texture2D(TextureLoader.loadTexture2D(src))
        }
    }

    init {
        bind()
        setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        setParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        unbind()
    }

    override fun bind() {
        glBindTexture(GL_TEXTURE_2D, id)
    }

    override fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    fun setParameter(name: Int, value: Int) {
        glTexParameteri(GL_TEXTURE_2D, name, value)
    }

    fun dispose() {
        glDeleteTextures(id)
    }
}