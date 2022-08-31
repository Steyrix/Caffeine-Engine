package engine.core.texture

import org.lwjgl.opengl.GL33C.*


class Texture2D(
    private var id: Int
) {

    companion object {
        fun createInstance(src: String): Texture2D {
            return Texture2D(TextureLoader.loadTexture2D(src))
        }
    }

    init {
        setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        setParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, id)
    }

    fun setParameter(name: Int, value: Int) {
        glTexParameteri(GL_TEXTURE_2D, name, value)
    }

    fun getId(): Int {
        return id
    }

    fun dispose() {
        glDeleteTextures(id)
    }
}