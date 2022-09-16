package engine.core.render.render2D

import engine.core.shader.Shader
import engine.core.texture.ArrayTexture2D
import engine.core.texture.Texture2D
import engine.core.update.Updatable
import engine.feature.animation.AnimationHolder2D

class AnimatedObject2D(
        bufferParamsCount: Int,
        dataArrays: List<FloatArray>,
        verticesCount: Int,
        texture: Texture2D? = null,
        arrayTexture: ArrayTexture2D? = null,
        private val animationHolder: AnimationHolder2D
) : OpenGlObject2D(bufferParamsCount, dataArrays, verticesCount, texture, arrayTexture), Updatable {

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