package engine.core.texture

import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer
import org.lwjgl.opengl.GL42C.*

data class LoadingResult(
        val width: Int,
        val height: Int,
        val buffer: ByteBuffer
)

object TextureLoader {

    private fun loadViaStbi(src: String): LoadingResult {
        val buff: ByteBuffer
        val width: Int
        val height: Int

        MemoryStack.stackPush().use {
            val tWidth = it.mallocInt(1)
            val tHeight = it.mallocInt(1)

            val channels = it.mallocInt(1)

            buff = STBImage.stbi_load(src, tWidth, tHeight, channels, 4)
                    ?: throw Exception("Image file [" + src + "] not loaded: " + STBImage.stbi_failure_reason())

            width = tWidth.get()
            height = tHeight.get()
        }

        return LoadingResult(
                width,
                height,
                buff
        )
    }
    fun loadTexture2D(src: String): Int {
        val loadingResult = loadViaStbi(src)

        val width = loadingResult.width
        val height = loadingResult.height
        val buff = loadingResult.buffer

        val id: Int = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, id)

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                width,
                height,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                buff
        )
        glGenerateMipmap(GL_TEXTURE_2D)
        glBindTexture(GL_TEXTURE_2D, 0)

        STBImage.stbi_image_free(buff)

        return id
    }

    fun loadArrayTexture2D(
            sources: List<String>,
            layersCount: Int
    ): Int {
        var width = 0
        var height = 0
        val buffers = mutableListOf<ByteBuffer>()

        sources.forEach { src ->
            val result = loadViaStbi(src)
            buffers.add(result.buffer)
            width = result.width
            height = result.height
        }

        val id: Int = glGenTextures()
        glBindTexture(GL_TEXTURE_2D_ARRAY, id)
        glTexStorage3D(
                GL_TEXTURE_2D_ARRAY,
                buffers.size,
                GL_RGBA8,
                width,
                height,
                layersCount
        )

        buffers.forEachIndexed { index, byteBuffer ->
            glTexSubImage3D(
                    GL_TEXTURE_2D_ARRAY,
                    0,
                    0,
                    0,
                    index,
                    width,
                    height,
                    layersCount,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    byteBuffer
            )
        }

        glBindTexture(GL_TEXTURE_2D_ARRAY, 0)

        buffers.forEach {
            STBImage.stbi_image_free(it)
        }

        return id
    }
}