package engine.feature.animation

import engine.core.entity.Entity
import engine.core.render.render2D.OpenGlObject2D
import engine.core.shader.Shader

class AnimationHolder2D(
        private var frameSizeX: Float,
        private var frameSizeY: Float,
        private val animations: MutableList<BasicAnimation>
) : Entity {
    private var currentAnimation: BasicAnimation = animations.first()

    fun updateAnimationUniforms(target: OpenGlObject2D, shader: Shader) {
        if (target.isTextured()) {
            shader.setUniform(Shader.VAR_KEY_X_OFFSET, currentAnimation.currentFrameX * frameSizeX)
            shader.setUniform(Shader.VAR_KEY_FRAME_X, currentAnimation.currentFrameX + 1)
            shader.setUniform(Shader.VAR_KEY_Y_OFFSET, currentAnimation.currentFrameY * frameSizeY)
            shader.setUniform(Shader.VAR_KEY_FRAME_Y, currentAnimation.currentFrameY + 1)
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