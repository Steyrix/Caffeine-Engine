package engine.core.controllable.animation

import engine.core.controllable.Controller

interface AnimationController : Controller {

    fun getAnimationKey(): String
}