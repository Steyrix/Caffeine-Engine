package engine.core.render


class CompositeAnimatedModel(
    private val animatedModels: List<AnimatedModel2D>
) : Animated, Zleveled  {

    override fun setAnimationByKey(key: String) {
        animatedModels.forEach {
            it.setAnimationByKey(key)
        }
    }

    override var zLevel: Float = animatedModels.maxOf { it.zLevel }
}