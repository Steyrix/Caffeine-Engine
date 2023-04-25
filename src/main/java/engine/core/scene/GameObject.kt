package engine.core.scene

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.core.window.Window

interface GameObject {
    var it: CompositeEntity?
    fun update(deltaTime: Float) {
        it?.update(deltaTime)
    }

    fun draw() {
        it?.draw()
    }

    fun input(window: Window) {
        it?.input(window)
    }

    fun addComponent(entity: Entity?, params: SetOfParameters) {
        if (entity == null) return
        it?.addComponent(entity, params)
    }

    fun isDisposed() = it?.isDisposed ?: false

    fun getZLevel(): Float = 0f
}