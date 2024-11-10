package engine.core.render

import engine.core.entity.Entity
import engine.feature.animation.Animation


class CompositeAnimatedModel(
    private val animatedModels: List<AnimatedModel2D>
) : Animated, Zleveled, Entity  {

    override var zLevel: Float = animatedModels.maxOf { it.zLevel }

    override fun setAnimationByKey(key: String) {
        animatedModels.forEach {
            it.setAnimationByKey(key)
        }
    }

    override fun resetAnimation(key: String) {
        animatedModels.forEach {
            it.resetAnimation(key)
        }
    }

    override fun changeAnimationSet(new: List<Animation>) {
        animatedModels.forEach {
            it.changeAnimationSet(new)
        }
    }
}