package engine.core.render

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.interfaces.Animated
import engine.core.render.interfaces.Zleveled
import engine.core.update.SetOfParameters
import engine.core.update.update2D.Parameterized
import engine.feature.animation.Animation


class CompositeAnimatedModel(
    private val animatedModels: MutableMap<String, AnimatedModel2D>
) : CompositeEntity(), Animated, Zleveled, Entity, Parameterized<SetOfParameters> {

    override var zLevel: Float = animatedModels.values.maxOf { it.zLevel }

    val models: Map<String, AnimatedModel2D>
        get() = animatedModels

    init {
        animatedModels.forEach {
            addComponent(it.value)
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

    fun addModel(key: String, model: AnimatedModel2D) {
        animatedModels[key] = model
        addComponent(model)
    }

    fun removeModel(key: String) {
        val target = animatedModels[key]
        removeComponent(target!!)
        animatedModels.remove(key)
    }

    fun combine(model: CompositeAnimatedModel) {
        model.animatedModels.forEach {
            addModel(it.key, it.value)
        }
    }

    fun removeAllOf(model: CompositeAnimatedModel) {
        model.animatedModels.keys.forEach {
            removeModel(it)
        }
    }
}