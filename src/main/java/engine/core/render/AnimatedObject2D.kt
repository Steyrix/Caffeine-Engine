package engine.core.render

import engine.core.shader.Shader
import engine.core.texture.ArrayTexture2D
import engine.core.texture.Texture2D
import engine.core.update.Updatable
import engine.feature.animation.Animation
import engine.feature.animation.AnimationHolder2D
import engine.feature.util.DefaultBufferData

class AnimatedObject2D(
        dataArrays: List<FloatArray>,
        verticesCount: Int = 6,
        texture: Texture2D? = null,
        arrayTexture: ArrayTexture2D? = null,
        private val animationHolder: AnimationHolder2D
) : OpenGlObject2D(dataArrays, verticesCount, texture, arrayTexture), Updatable {

    constructor(
            frameSizeX: Float,
            frameSizeY: Float,
            texture: Texture2D?,
            animations: List<Animation>
    ) : this(
            dataArrays = listOf(DefaultBufferData.RECTANGLE_INDICES, DefaultBufferData.getRectangleSectorVertices(frameSizeX, frameSizeY)),
            verticesCount = 6,
            texture,
            arrayTexture = null,
            animationHolder = AnimationHolder2D(animations)
    )

    constructor(
            frameSizeX: Float,
            frameSizeY: Float,
            arrayTexture: ArrayTexture2D?,
            animations: MutableList<Animation>
    ) : this(
            dataArrays = listOf(DefaultBufferData.RECTANGLE_INDICES, DefaultBufferData.getRectangleSectorVertices(frameSizeX, frameSizeY)),
            verticesCount = 6,
            texture = null,
            arrayTexture,
            animationHolder = AnimationHolder2D(animations)
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