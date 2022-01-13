package engine.core.texture

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.system.MemoryStack.stackPush
import java.nio.ByteBuffer
import org.lwjgl.stb.STBImage.*


class Texture2D(
    private var id: Int
) {

    companion object {
        fun createInstance(src: String): Texture2D {
            return Texture2D(loadTexture(src))
        }

        private fun loadTexture(src: String): Int {
            val width: Int
            val height: Int

            val buff: ByteBuffer

            stackPush().use {
                val tWidth = it.mallocInt(1)
                val tHeight = it.mallocInt(1)

                val channels = it.mallocInt(1)

                buff = stbi_load(src, tWidth, tHeight, channels, 4) ?:
                        throw Exception("Image file [" + src  + "] not loaded: " + stbi_failure_reason())

                width = tWidth.get()
                height = tHeight.get()
            }

            val id: Int = glGenTextures()
            glBindTexture(id, GL_TEXTURE_2D)

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buff)
            glGenerateMipmap(GL_TEXTURE_2D)

            stbi_image_free(buff)

            return id
        }
    }


    fun bind() {
        glBindTexture(id, GL_TEXTURE_2D)
    }

    fun getId(): Int {
        return id
    }

    fun dispose() {
        glDeleteTextures(id)
    }
}