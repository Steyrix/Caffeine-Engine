package engine.core.scene

import engine.core.window.Window

interface Scene {
    fun init()

    fun input(window: Window)

    fun update(deltaTime: Float)

    fun render(window: Window)
}