package demo.labyrinth.data.gameobject

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.update.SetOfParameters

interface GameObject {
    var it: CompositeEntity?
    fun update(deltaTime: Float) {
        it?.update(deltaTime)
    }

    fun draw() {
        it?.draw()
    }

    fun addComponent(entity: Entity?, params: SetOfParameters) {
        if (entity == null) return
        it?.addComponent(entity, params)
    }

    fun isDisposed() = it?.isDisposed ?: false
}