package engine.core.scene.game_object

import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.core.window.Window

interface GameObject {

    fun update(deltaTime: Float)

    fun draw()

    fun input(window: Window)

    fun addComponent(entity: Entity?, params: SetOfParameters)

    fun isDisposed(): Boolean

    fun getZLevel(): Float
}