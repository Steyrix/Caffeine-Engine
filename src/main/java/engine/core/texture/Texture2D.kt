package engine.core.texture

import org.lwjgl.opengl.GL33C.*
import java.awt.Dimension

class Texture2D(
        override val id: Int
) : Texture {

    private val dimension: Dimension
    companion object {
        fun createInstance(src: String): Texture2D {
            return Texture2D(TextureLoader.loadTexture2D(src))
        }
    }

    init {
        bind()
        dimension = getSize()
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
        bind()
        glTexParameteri(GL_TEXTURE_2D, name, value)
        unbind()
    }

    fun dispose() {
        glDeleteTextures(id)
    }

    fun getWidthF(): Float {
        return dimension.width.toFloat()
    }

    fun getHeightF(): Float {
        return dimension.height.toFloat()
    }

    private fun getSize(): Dimension {
        val arrayW = IntArray(1)
        val arrayH = IntArray(1)
        glGetTexLevelParameteriv(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH, arrayW)
        glGetTexLevelParameteriv(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT, arrayH)
        return Dimension(arrayW.first(), arrayH.first())
    }
}