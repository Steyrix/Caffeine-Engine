package engine.core.texture

import org.lwjgl.opengl.GL33C.*
import org.lwjgl.system.MemoryStack.stackPush
import java.nio.ByteBuffer
import org.lwjgl.stb.STBImage.*


class Texture2D(
    private var id: Int
) {

    companion object {
        fun createInstance(src: String): Texture2D {
            return Texture2D(TextureLoader.loadTexture(src))
        }
    }

    fun bind() {
        glBindTexture(id, GL_TEXTURE_2D)
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