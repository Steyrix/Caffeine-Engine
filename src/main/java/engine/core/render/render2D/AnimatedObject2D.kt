package engine.core.render.render2D

import engine.core.shader.Shader
import engine.core.texture.ArrayTexture2D
import engine.core.texture.Texture2D
import engine.feature.animation.AnimationHolder2D

class AnimatedObject2D(
        bufferParamsCount: Int,
        dataArrays: List<FloatArray>,
        verticesCount: Int,
        texture: Texture2D? = null,
        arrayTexture: ArrayTexture2D? = null,
        protected val animationHolder: AnimationHolder2D
) : OpenGlObject2D(bufferParamsCount, dataArrays, verticesCount, texture, arrayTexture) {

    override var shader: Shader? = null
        set(value) {
            field = value
            field?.let {
                animationHolder.updateAnimationUniforms(
                        target = this,
                        shader = it
                )
            }
        }

    fun playAnimation(deltaTime: Float) {
        animationHolder.playAnimation(deltaTime)
    }
}