package engine.core.render.render2D

import engine.core.shader.Shader
import engine.core.texture.ArrayTexture2D
import engine.core.texture.Texture2D
import engine.core.update.Updatable
import engine.feature.animation.AnimationHolder2D
import engine.feature.util.Buffer

class AnimatedObject2D(
        bufferParamsCount: Int = 2,
        dataArrays: List<FloatArray>,
        verticesCount: Int = 6,
        texture: Texture2D? = null,
        arrayTexture: ArrayTexture2D? = null,
        private val animationHolder: AnimationHolder2D
) : OpenGlObject2D(bufferParamsCount, dataArrays, verticesCount, texture, arrayTexture), Updatable {

    constructor(
            frameSizeX: Float,
            frameSizeY: Float,
            texture: Texture2D?,
            animationHolder: AnimationHolder2D
    ) : this(
            bufferParamsCount = 2,
            dataArrays = listOf(Buffer.RECTANGLE_INDICES, Buffer.getRectangleSectorVertices(frameSizeX, frameSizeY)),
            verticesCount = 6,
            texture,
            arrayTexture = null,
            animationHolder
    )

    constructor(
            frameSizeX: Float,
            frameSizeY: Float,
            arrayTexture: ArrayTexture2D?,
            animationHolder: AnimationHolder2D
    ) : this(
            bufferParamsCount = 2,
            dataArrays = listOf(Buffer.RECTANGLE_INDICES, Buffer.getRectangleSectorVertices(frameSizeX, frameSizeY)),
            verticesCount = 6,
            texture = null,
            arrayTexture,
            animationHolder
    )

    override var shader: Shader? = null

    override fun update(deltaTime: Float) {
        shader?.let {
            it.bind()
            animationHolder.updateAnimationUniforms(this, it)
        }
        animationHolder.playAnimation(deltaTime)
    }

    fun setAnimationByKey(key: String) {
        animationHolder.setAnimationByKey(key)
    }
}