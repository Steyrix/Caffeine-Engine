package engine.core.scene

import engine.core.game_object.CompositeGameEntity
import engine.core.game_object.GameEntity

open class GameContext {

    companion object {
        fun getInstance(): GameContext {
            return GameContext()
        }
    }

    private val entities: MutableList<GameEntity> = mutableListOf()

    fun add(entity: GameEntity) = entities.add(entity)
    fun addAll(list: List<GameEntity>) = entities.addAll(list)

    fun remove(entity: GameEntity) = entities.remove(entity)

    fun forEach(block: (GameEntity) -> Unit) = entities.forEach(block)

    fun find(predicate: (GameEntity) -> Boolean) = entities.find { predicate(it) }

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

    fun update(deltaTime: Float) {
        val iterator = entities.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.isDisposed()) {
                iterator.remove()
            } else {
                item.update(deltaTime)
            }
        }
    }
}