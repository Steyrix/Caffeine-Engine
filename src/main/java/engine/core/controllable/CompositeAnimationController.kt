package engine.core.controllable

import engine.core.render.AnimatedModel2D

abstract class CompositeAnimationController(
    protected var drawableComponents: Map<String, AnimatedModel2D>
) : Controller {

    abstract fun getAnimationByKey(bodyPartKey: String): String

    override fun update(deltaTime: Float) {
        drawableComponents.entries.forEach {
            it.value.setAnimationByKey(it.key)
        }
    }
}