package demo.medieval_game.data.gameobject.npc

import demo.medieval_game.data.AnimationKey
import engine.core.controllable.Direction
import java.util.*

object HumanoidAnimationMaps {
    fun getIdleMap() =
            EnumMap<Direction, String>(Direction::class.java).apply {
                put(Direction.RIGHT, AnimationKey.IDLE_R)
                put(Direction.LEFT, AnimationKey.IDLE_L)
                put(Direction.UP, AnimationKey.IDLE_U)
                put(Direction.DOWN, AnimationKey.IDLE_D)
            }

    fun getStrikeMap() =
            EnumMap<Direction, String>(Direction::class.java).apply {
                put(Direction.RIGHT, AnimationKey.STRIKE_R)
                put(Direction.LEFT, AnimationKey.STRIKE_L)
                put(Direction.UP, AnimationKey.STRIKE_U)
                put(Direction.DOWN, AnimationKey.STRIKE_D)
            }

    fun getWalkMap() =
            EnumMap<Direction, String>(Direction::class.java).apply {
                put(Direction.RIGHT, AnimationKey.WALK_R)
                put(Direction.LEFT, AnimationKey.WALK_L)
                put(Direction.UP, AnimationKey.WALK_U)
                put(Direction.DOWN, AnimationKey.WALK_D)
            }
}