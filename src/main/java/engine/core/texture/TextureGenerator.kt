package engine.core.texture

import engine.core.render.Model
import org.lwjgl.opengl.GL32.*

internal object TextureGenerator {

    fun createFromFBO(
        width: Float,
        height: Float,
        model: Model
    ): Int {
        val frameBufferName: Int = glGenFramebuffers()
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName)

        val renderTexture: Int = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, renderTexture)

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.toInt() * 2, height.toInt() * 2, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0)
        glGenerateMipmap(GL_TEXTURE_2D)

        glBindTexture(GL_TEXTURE_2D, 0)

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, renderTexture, 0)
        glDrawBuffers(GL_COLOR_ATTACHMENT0)

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw IllegalStateException("Texture loading failed")
        }
        println(glCheckFramebufferStatus(GL_FRAMEBUFFER))
        glViewport(0, 0, width.toInt() * 2, height.toInt() * 2)
        glClear(GL_COLOR_BUFFER_BIT)
        model.draw()
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glViewport(0, 0, width.toInt() * 2, height.toInt() * 2)
        glClear(GL_COLOR_BUFFER_BIT)

        return renderTexture
    }
}