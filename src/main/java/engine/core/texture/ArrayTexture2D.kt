package engine.core.texture

import org.lwjgl.opengl.GL42C.*

class ArrayTexture2D(
        override val id: Int
) : Texture {

    companion object {
        fun createInstance(
                sources: List<String>,
                layersCount: Int
        ): ArrayTexture2D {
            return ArrayTexture2D(
                    TextureLoader.loadArrayTexture2D(sources, layersCount)
            )
        }
    }

    override val bindTarget = GL_TEXTURE_2D_ARRAY

    init {
        bind()
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR)
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        unbind()
    }

    fun dispose() {
        glDeleteTextures(id)
    }
}