package demo.medieval_game.data.gameobject.npc

import engine.core.controllable.AnimationController
import engine.core.controllable.Direction
import java.util.EnumMap

typealias MultiMap = HashMap<String, MutableMap<Direction, String>>

abstract class HumanoidController(
        idleMap: EnumMap<Direction, String>,
        strikeMap: EnumMap<Direction, String>,
        walkMap: EnumMap<Direction, String>,
) : AnimationController {

    companion object {
        const val IDLE = "IDLE"
        const val STRIKE = "STRIKE"
        const val WALK = "WALK"
    }

    protected var direction: Direction = Direction.LEFT
    var isStriking = false
    var isWalking = false

    private val animationMultiMap: MultiMap = hashMapOf(
            IDLE to idleMap,
            STRIKE to strikeMap,
            WALK to walkMap
    )

    override fun getAnimationKey(): String {
        return when {
            isStriking -> getStrikingAnimation()
            isWalking -> getWalkingAnimation()
            else -> getIdleAnimation()
        }
    }

    private fun getStrikingAnimation(): String {
        return animationMultiMap[STRIKE]?.get(direction) ?: ""
    }

    private fun getWalkingAnimation(): String {
        return animationMultiMap[WALK]?.get(direction) ?: ""
    }

    private fun getIdleAnimation(): String {
        return animationMultiMap[IDLE]?.get(direction) ?: ""
    }
}