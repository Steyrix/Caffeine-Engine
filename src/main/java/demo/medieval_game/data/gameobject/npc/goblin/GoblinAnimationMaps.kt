package demo.medieval_game.data.gameobject.npc.goblin

import demo.medieval_game.data.AnimationKey
import engine.core.controllable.Direction
import java.util.*

object GoblinAnimationMaps {
    val idleMap =
            EnumMap<Direction, String>(Direction::class.java).apply {
                put(Direction.RIGHT, AnimationKey.GOBLIN_IDLE_R)
                put(Direction.LEFT, AnimationKey.GOBLIN_IDLE_L)
                put(Direction.UP, AnimationKey.GOBLIN_IDLE_U)
                put(Direction.DOWN, AnimationKey.GOBLIN_IDLE_D)
            }

    val strikeMap =
            EnumMap<Direction, String>(Direction::class.java).apply {
                put(Direction.RIGHT, AnimationKey.GOBLIN_STRIKE_R)
                put(Direction.LEFT, AnimationKey.GOBLIN_STRIKE_L)
                put(Direction.UP, AnimationKey.GOBLIN_STRIKE_U)
                put(Direction.DOWN, AnimationKey.GOBLIN_STRIKE_D)
            }

    val walkMap =
            EnumMap<Direction, String>(Direction::class.java).apply {
                put(Direction.RIGHT, AnimationKey.GOBLIN_WALK_R)
                put(Direction.LEFT, AnimationKey.GOBLIN_WALK_L)
                put(Direction.UP, AnimationKey.GOBLIN_WALK_U)
                put(Direction.DOWN, AnimationKey.GOBLIN_WALK_D)
            }
}