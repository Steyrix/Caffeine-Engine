package engine.core.scene

import engine.core.game_object.CompositeGameEntity
import engine.core.game_object.GameEntity

interface GameContext {

    companion object {
        fun getInstance(): GameContext {
            return object : GameContext {
                override val entities: MutableList<GameEntity> = mutableListOf()
            }
        }
    }

    val entities: MutableList<GameEntity>

    fun add(entity: GameEntity) {
        entities.add(entity)
    }

    fun remove(entity: GameEntity) {
        entities.remove(entity)
    }

    fun entitiesSortedByLevelZ(): List<GameEntity> {
        val out = mutableListOf<GameEntity>()
        entities.forEach {
            if (it is CompositeGameEntity) {
                out.addAll(it.getInnerObjects())
            } else {
                out.add(it)
            }
        }

        return out.sortedBy { it.getZLevel() }
    }
}