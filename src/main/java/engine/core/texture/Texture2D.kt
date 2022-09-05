package engine.core.texture

import org.lwjgl.opengl.GL11C
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
        glTexParameteri(GL_TEXTURE_2D, name, value)
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
        val array = intArrayOf()
        glGetTexParameteriv(GL_TEXTURE_2D, GL_TEXTURE_WIDTH, array)
        glGetTexParameteriv(GL_TEXTURE_2D, GL_TEXTURE_HEIGHT, array)
        return Dimension(array.first(), array.last())
    }
}