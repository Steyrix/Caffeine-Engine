package engine.core.render

import engine.core.entity.Entity


class CompositeAnimatedModel(
    private val animatedModels: List<AnimatedModel2D>
) : Animated, Zleveled, Entity  {

    override fun setAnimationByKey(key: String) {
        animatedModels.forEach {
            it.setAnimationByKey(key)
        }
    }

    override var zLevel: Float = animatedModels.maxOf { it.zLevel }
}