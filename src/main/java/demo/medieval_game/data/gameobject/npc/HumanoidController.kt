package demo.medieval_game.data.gameobject.npc

import engine.core.controllable.AnimationController
import engine.core.controllable.Direction

abstract class HumanoidController : AnimationController {

    protected var direction: Direction = Direction.LEFT
    protected var isStriking = false
    protected var isWalking = false

    override fun getAnimationKey(): String {
        return when {
            isStriking -> getStrikingAnimation()
            isWalking -> getWalkingAnimation()
            else -> getIdleAnimation()
        }
    }

    // TODO: default impl of these methods
    protected abstract fun getStrikingAnimation(): String

    protected abstract fun getWalkingAnimation(): String

    protected abstract fun getIdleAnimation(): String
}