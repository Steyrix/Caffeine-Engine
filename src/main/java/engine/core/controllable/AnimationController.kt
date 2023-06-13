package engine.core.controllable

import engine.core.controllable.Controller

interface AnimationController : Controller {

    fun getAnimationKey(): String
}