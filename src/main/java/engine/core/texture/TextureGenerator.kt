package engine.core.texture

import engine.core.render.Model
import org.lwjgl.opengl.GL32.*
import java.nio.ByteBuffer

object TextureGenerator {

    fun createFromFBO(
        width: Float,
        height: Float,
        model: Model
    ): Int {
        val frameBufferName: Int = glGenFramebuffers()
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName)

        val nullBuffer: ByteBuffer? = null

        val renderTexture: Int = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, renderTexture)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.toInt(), height.toInt(), 0, GL_RGBA, GL_UNSIGNED_BYTE, nullBuffer)

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, renderTexture, 0)

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            return -1
        }

        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName)
        model.draw()
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glBindTexture(GL_TEXTURE_2D, 0)

        return renderTexture
    }
}