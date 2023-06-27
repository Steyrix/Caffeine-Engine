package engine.core.render

import engine.core.texture.ArrayTexture2D
import engine.core.texture.Texture2D
import engine.core.update.Updatable
import engine.feature.animation.Animation
import engine.feature.animation.AnimationHolder2D
import engine.core.render.util.DefaultBufferData

class AnimatedModel2D(
        mesh: Mesh,
        texture: Texture2D? = null,
        arrayTexture: ArrayTexture2D? = null,
        private val animationHolder: AnimationHolder2D
) : Model(mesh, texture, arrayTexture), Updatable {

    constructor(
            frameSizeX: Float,
            frameSizeY: Float,
            texture: Texture2D?,
            animations: List<Animation>
    ) : this(
            mesh = Mesh(
                    dataArrays = listOf(DefaultBufferData.RECTANGLE_INDICES, DefaultBufferData.getRectangleSectorVertices(frameSizeX, frameSizeY)),
                    verticesCount = 6
            ),
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
            mesh = Mesh(
                    dataArrays = listOf(DefaultBufferData.RECTANGLE_INDICES, DefaultBufferData.getRectangleSectorVertices(frameSizeX, frameSizeY)),
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