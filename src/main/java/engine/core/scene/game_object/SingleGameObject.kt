package engine.core.scene.game_object

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.core.window.Window

open class SingleGameObject : GameObject {

    var it: CompositeEntity? = null

    override fun update(deltaTime: Float) {
        it?.update(deltaTime)
    }

    override fun draw() {
        it?.draw()
    }

    override fun input(window: Window) {
        it?.input(window)
    }

    fun addComponent(entity: Entity?, params: SetOfParameters) {
        if (entity == null) return
        it?.addComponent(entity, params)
    }

    override fun isDisposed() = it?.isDisposed ?: false

    override fun getZLevel(): Float = 0f
}