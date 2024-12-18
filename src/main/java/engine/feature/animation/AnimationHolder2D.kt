package engine.feature.animation

import engine.core.render.model.Model
import engine.core.render.shader.Shader

class AnimationHolder2D(
    private val animations: MutableList<Animation>
) {

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

    fun resetAnimation(key: String) {
        val target = animations.firstOrNull { it.name == key } ?: currentAnimation
        target.reset()
    }

    fun changeAnimationSet(new: List<Animation>) {
        animations.clear()
        animations.addAll(new)
        currentAnimation = animations.first()
    }
}