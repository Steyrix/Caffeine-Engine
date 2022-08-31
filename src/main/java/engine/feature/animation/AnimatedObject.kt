package engine.feature.animation

import engine.core.render.render2D.OpenGlObject2D
import engine.core.shader.Shader

class AnimatedObject(
        private var frameSizeX: Float,
        private var frameSizeY: Float,
        private val animations: MutableList<BasicAnimation>
) {
    private var currentAnimation: BasicAnimation = animations.first()

    fun defineAnimationVariables(graphicalObject: OpenGlObject2D, shader: Shader) {
        if (graphicalObject.isTextured()) {
            shader.setUniform(Shader.VAR_KEY_X_OFFSET, currentAnimation.currentFrameX * frameSizeX)
            shader.setUniform(Shader.VAR_KEY_FRAME_X, currentAnimation.currentFrameX + 1)
            shader.setUniform(Shader.VAR_KEY_Y_OFFSET, currentAnimation.currentFrameY * frameSizeY)
            shader.setUniform(Shader.VAR_KEY_FRAME_Y, currentAnimation.currentFrameY + 1)
        }
    }

    fun playAnimation(deltaTime: Float) {
        currentAnimation.play(deltaTime)
    }

    fun setAnimation(a: BasicAnimation) {
        if (animations.contains(a)) {
            currentAnimation = a
        }
    }
}