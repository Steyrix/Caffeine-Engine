package engine.feature.animation

import engine.core.entity.Entity
import engine.core.render.render2D.OpenGlObject2D
import engine.core.shader.Shader

// TODO: think of a way to use different Animation.kt implementations
class AnimationHolder2D(
        private val animations: MutableList<Animation>
) : Entity {
    private var currentAnimation: Animation = animations.first()

    fun updateAnimationUniforms(target: OpenGlObject2D, shader: Shader) {
        if (target.isTextured()) {
            currentAnimation.setUniforms(shader)
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