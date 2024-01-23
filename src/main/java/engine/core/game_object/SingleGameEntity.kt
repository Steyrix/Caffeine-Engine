package engine.core.game_object

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.loop.PredicateTimeEvent
import engine.core.update.SetOfParameters
import engine.core.window.Window

open class SingleGameEntity : GameEntity {

    var it: CompositeEntity? = null

    protected val gameLoopEvents = listOf<PredicateTimeEvent>()

    override fun update(deltaTime: Float) {
        it?.update(deltaTime)
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

    override fun isDisposed() = it?.isDisposed ?: false

    override fun getZLevel(): Float = 0f
}