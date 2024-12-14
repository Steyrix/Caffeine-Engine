package engine.core.texture

import engine.core.render.model.Model
import org.lwjgl.opengl.GL33C.*
import java.awt.Dimension

class Texture2D(
    override val id: Int
) : Texture {

    override val bindTarget = GL_TEXTURE_2D

    private val dimension: Dimension

    companion object {
        fun createInstance(src: String): Texture2D {
            return Texture2D(TextureLoader.loadTexture2D(src))
        }

        fun createInstance(
            precision: Float,
            screenWidth: Float,
            screenHeight: Float,
            model: Model
        ): Texture2D {
            return Texture2D(
                TextureGenerator.createFromFBO(
                    precision,
                    screenWidth,
                    screenHeight,
                    model
                )
            )
        }
    }

    init {
        dimension = getSize()
        setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        setParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
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
        bind()
        val arrayW = IntArray(1)
        val arrayH = IntArray(1)
        glGetTexLevelParameteriv(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH, arrayW)
        glGetTexLevelParameteriv(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT, arrayH)
        unbind()
        return Dimension(arrayW.first(), arrayH.first())
    }
}