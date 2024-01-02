package engine.core.game_object

import engine.core.window.Window

interface GameEntity {

    fun update(deltaTime: Float)

    fun draw()

    fun input(window: Window)

    fun isDisposed(): Boolean
}