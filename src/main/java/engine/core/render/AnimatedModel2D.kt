package engine.core.render

import engine.core.ResourceLoader
import engine.core.render.interfaces.Animated
import engine.core.texture.ArrayTexture2D
import engine.core.texture.Texture2D
import engine.core.update.Updatable
import engine.feature.animation.Animation
import engine.feature.animation.AnimationHolder2D
import engine.core.render.util.DefaultBufferData
import engine.feature.animation.AtlasData

open class AnimatedModel2D(
    mesh: Mesh,
    texture: Texture2D? = null,
    arrayTexture: ArrayTexture2D? = null,
    private val animationHolder: AnimationHolder2D
) : Model(mesh, texture, arrayTexture), Updatable, Animated {

    constructor(
        frameWidth: Float,
        frameHeight: Float,
        texture: Texture2D?,
        animations: List<Animation>
    ) : this(
        mesh = Mesh(
            dataArrays = DefaultBufferData.getRectangleSectorBuffers(frameWidth, frameHeight),
            verticesCount = 6
        ),
        texture,
        arrayTexture = null,
        animationHolder = AnimationHolder2D(animations.toMutableList())
    )

    constructor(
        frameWidth: Float,
        frameHeight: Float,
        arrayTexture: ArrayTexture2D?,
        animations: MutableList<Animation>
    ) : this(
        mesh = Mesh(
            dataArrays = DefaultBufferData.getRectangleSectorBuffers(frameWidth, frameHeight),
            verticesCount = 6
        ),
        texture = null,
        arrayTexture,
        animationHolder = AnimationHolder2D(animations)
    )

    constructor(
        dataArrays: List<FloatArray>,
        verticesCount: Int,
        texture: Texture2D? = null,
        arrayTexture: ArrayTexture2D? = null,
        animationHolder: AnimationHolder2D
    ) : this(
        mesh = Mesh(dataArrays, verticesCount),
        texture = texture,
        arrayTexture = arrayTexture,
        animationHolder = animationHolder
    )

    constructor(
        data: AtlasData,
        texture: Texture2D? = null,
    ) : this(
        mesh = Mesh(
            dataArrays = DefaultBufferData.getRectangleSectorBuffers(data.frameWidth, data.frameHeight),
            verticesCount = 6
        ),
        texture,
        arrayTexture = null,
        animationHolder = AnimationHolder2D(data.animations.toMutableList())
    )

    constructor(
        data: AtlasData
    ) : this(
        mesh = Mesh(
            dataArrays = DefaultBufferData.getRectangleSectorBuffers(data.frameWidth, data.frameHeight),
            verticesCount = 6
        ),
        texture = ResourceLoader.loadTexture(data.texturePath!!),
        arrayTexture = null,
        animationHolder = AnimationHolder2D(data.animations.toMutableList())
    )

    override fun update(deltaTime: Float) {
        shader?.let {
            it.bind()
            animationHolder.updateAnimationUniforms(this, it)
        }

        stencilShader?.let {
            it.bind()
            animationHolder.updateAnimationUniforms(this, it)
        }
        animationHolder.playAnimation(deltaTime)
    }

    fun setAnimationByKey(animationKey: String) {
        animationHolder.setAnimationByKey(animationKey)
    }

    override fun resetAnimation(key: String) {
        animationHolder.resetAnimation(key)
    }

    override fun changeAnimationSet(new: List<Animation>) {
        animationHolder.changeAnimationSet(new)
    }
}