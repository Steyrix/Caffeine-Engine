package engine.core.controllable

import engine.core.render.Animated

abstract class AnimationController(
    protected var drawableComponent: Animated
) : Controller {

    abstract fun getAnimationKey(): String

    override fun update(deltaTime: Float) {
        drawableComponent.setAnimationByKey(getAnimationKey())
    }
}