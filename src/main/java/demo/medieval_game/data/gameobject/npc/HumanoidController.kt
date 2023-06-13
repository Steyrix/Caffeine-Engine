package demo.medieval_game.data.gameobject.npc

import engine.core.controllable.AnimationController
import engine.core.controllable.Direction
import java.util.EnumMap

private const val IDLE = "IDLE"
private const val STRIKE = "STRIKE"
private const val WALK = "WALK"

typealias MultiMap = HashMap<String, MutableMap<Direction, String>>

abstract class HumanoidController : AnimationController {

    protected var direction: Direction = Direction.LEFT
    protected var isStriking = false
    protected var isWalking = false

    protected val animationMultiMap: MultiMap = hashMapOf(
            IDLE to EnumMap(Direction::class.java),
            STRIKE to EnumMap(Direction::class.java),
            WALK to EnumMap(Direction::class.java)
    )

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