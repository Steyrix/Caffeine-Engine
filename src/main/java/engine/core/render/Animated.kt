package engine.core.render

import engine.feature.animation.Animation

interface Animated {

    fun setAnimationByKey(key: String)

    fun resetAnimation(key: String)

    fun changeAnimationSet(new: List<Animation>)
}