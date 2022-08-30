package engine.core.texture

import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer
import org.lwjgl.opengl.GL33C.*

object TextureLoader {

    fun loadTexture2D(src: String): Int {
        val width: Int
        val height: Int

        val buff: ByteBuffer

        MemoryStack.stackPush().use {
            val tWidth = it.mallocInt(1)
            val tHeight = it.mallocInt(1)

            val channels = it.mallocInt(1)

            buff = STBImage.stbi_load(src, tWidth, tHeight, channels, 4)
                    ?: throw Exception("Image file [" + src + "] not loaded: " + STBImage.stbi_failure_reason())

            width = tWidth.get()
            height = tHeight.get()
        }

        val id: Int = glGenTextures()
        glBindTexture(id, GL_TEXTURE_2D)

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buff)
        glGenerateMipmap(GL_TEXTURE_2D)

        STBImage.stbi_image_free(buff)

        return id
    }
}