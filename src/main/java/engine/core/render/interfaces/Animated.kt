package engine.core.render.interfaces

import engine.feature.animation.Animation

interface Animated {

    fun resetAnimation(key: String)

    fun changeAnimationSet(new: List<Animation>)
}