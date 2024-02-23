package demo.medieval_game.data.gameobject.npc

import engine.core.controllable.AnimationController
import engine.core.controllable.Direction
import engine.core.render.AnimatedModel2D
import java.util.EnumMap

typealias MultiMap = HashMap<String, MutableMap<Direction, String>>

abstract class HumanoidAnimationController(
    drawableComponent: AnimatedModel2D,
    idleMap: EnumMap<Direction, String>,
    strikeMap: EnumMap<Direction, String>,
    walkMap: EnumMap<Direction, String>,
) : AnimationController(drawableComponent) {

    companion object {
        const val IDLE = "IDLE"
        const val STRIKE = "STRIKE"
        const val ALT_STRIKE = "ALT_STRIKE"
        const val WALK = "WALK"
    }

    var direction: Direction = Direction.LEFT
    var isStriking = false
    var isWalking = false

    var isAltStrike = false

    // TODO: move out later
    private val altStrikeMap = HumanoidAnimationMaps.getCharacterAlternativeStrikeMap()

    private val animationMultiMap: MultiMap = hashMapOf(
        IDLE to idleMap,
        STRIKE to strikeMap,
        ALT_STRIKE to altStrikeMap,
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
        return if (isAltStrike) {
            animationMultiMap[ALT_STRIKE]?.get(direction) ?: ""
        } else {
            animationMultiMap[STRIKE]?.get(direction) ?: ""
        }
    }

    private fun getWalkingAnimation(): String {
        return animationMultiMap[WALK]?.get(direction) ?: ""
    }

    private fun getIdleAnimation(): String {
        return animationMultiMap[IDLE]?.get(direction) ?: ""
    }
}