package engine.core.game_object

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.loop.GameLoopTimeEvent
import engine.core.update.SetOfParameters
import engine.core.window.Window

open class SingleGameEntity : GameEntity {

    var it: CompositeEntity? = null

    private val gameLoopEvents = mutableListOf<GameLoopTimeEvent>()
    private val eventsToRemove = mutableListOf<GameLoopTimeEvent>()

    override var isSpawned: Boolean = false

    override fun update(deltaTime: Float) {
        it?.update(deltaTime)
        val iterator = gameLoopEvents.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (eventsToRemove.contains(item)) {
                iterator.remove()
                eventsToRemove.remove(item)
            } else {
                item.schedule(deltaTime)
            }
        }
    }

    override fun draw() {
        it?.draw()
    }

    override fun input(window: Window) {
        it?.input(window)
    }

    fun addComponent(entity: Entity?, params: SetOfParameters): SingleGameEntity {
        if (entity != null) {
            it?.addComponent(entity, params)
        }

        return this
    }

    fun addEvent(event: GameLoopTimeEvent) {
        gameLoopEvents.add(event)
    }

    fun removeEvent(event: GameLoopTimeEvent) {
        eventsToRemove.add(event)
    }

    override fun isDisposed() = it?.isDisposed ?: false

    override fun getZLevel(): Float = 0f
    override fun preSpawn(spawnOptions: SpawnOptions) = Unit
}