package engine.feature.animation

import engine.core.entity.Entity
import engine.core.render.Model
import engine.core.shader.Shader

class AnimationHolder2D(
        private val animations: List<Animation>
) : Entity {
    private var currentAnimation: Animation = animations.first()

    fun updateAnimationUniforms(target: Model, shader: Shader) {
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