package engine.core.render

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.core.update.update2D.Parameterized
import engine.feature.animation.Animation


class CompositeAnimatedModel(
    private val animatedModels: List<AnimatedModel2D>
) : CompositeEntity(), Animated, Zleveled, Entity, Parameterized<SetOfParameters> {

    override var zLevel: Float = animatedModels.maxOf { it.zLevel }

    init {
        animatedModels.forEach {
            this.addComponent(it)
        }
    }

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

    override fun updateParameters(parameters: SetOfParameters) {
        animatedModels.forEach {
            it.updateParameters(parameters)
        }
    }
}