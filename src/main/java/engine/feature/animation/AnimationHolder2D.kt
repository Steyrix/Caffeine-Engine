package engine.feature.animation

import engine.core.entity.Entity
import engine.core.render.render2D.OpenGlObject2D
import engine.core.shader.Shader

// TODO: think of a way to use different Animation.kt implementations
class AnimationHolder2D(
        private val animations: MutableList<SequenceAtlasAnimation>
) : Entity {
    private var currentAnimation: SequenceAtlasAnimation = animations.first()

    fun updateAnimationUniforms(target: OpenGlObject2D, shader: Shader) {
        if (target.isTextured()) {
            val params = currentAnimation.currentFrame!!

            shader.setUniform(Shader.VAR_KEY_X_OFFSET, params.xOffset)
            shader.setUniform(Shader.VAR_KEY_FRAME_X, params.frameNumberX)
            shader.setUniform(Shader.VAR_KEY_Y_OFFSET, params.yOffset)
            shader.setUniform(Shader.VAR_KEY_FRAME_Y, params.frameNumberY)
        }
    }

    fun playAnimation(deltaTime: Float) {
        currentAnimation.play(deltaTime)
    }

    fun setAnimationByKey(key: String) {
        if (currentAnimation.name == key) return
        currentAnimation = animations.firstOrNull { it.name == key } ?: currentAnimation
    }
}