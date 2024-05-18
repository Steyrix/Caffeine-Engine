package engine.core.controllable

import engine.core.render.AnimatedModel2D

abstract class AnimationController(
    protected var drawableComponent: AnimatedModel2D
) : Controller {

    abstract fun getAnimationKey(): String

    override fun update(deltaTime: Float) {
        drawableComponent.setAnimationByKey(getAnimationKey())
    }
}