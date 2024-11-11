package engine.core.controllable

import engine.core.render.Animated

abstract class AnimationController(
    protected var drawableComponent: Animated
) : Controller {

    abstract fun getAnimationKey(): String

    abstract fun getAnimationKeysForComposite(keys: String): List<String>

    override fun update(deltaTime: Float) {
        drawableComponent.setAnimationByKey(getAnimationKey())
    }
}