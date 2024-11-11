package engine.core.render

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.core.update.update2D.Parameterized
import engine.feature.animation.Animation


class CompositeAnimatedModel(
    val animatedModels: Map<String, AnimatedModel2D>,
    ordering: List<String>
) : CompositeEntity(), Animated, Zleveled, Entity, Parameterized<SetOfParameters> {

    override var zLevel: Float = animatedModels.values.maxOf { it.zLevel }

    init {
        ordering.forEach {
            addComponent(animatedModels[it] as Entity)
        }
    }

    fun setAnimationByKey(bodyPartKey: String, animationKey: String) {
        animatedModels[bodyPartKey]?.setAnimationByKey(animationKey)
    }

    override fun resetAnimation(key: String) {
        animatedModels.forEach {
            it.value.resetAnimation(key)
        }
    }

    override fun changeAnimationSet(new: List<Animation>) {
        animatedModels.forEach {
            it.value.changeAnimationSet(new)
        }
    }

    override fun updateParameters(parameters: SetOfParameters) {
        animatedModels.forEach {
            it.value.updateParameters(parameters)
        }
    }
}