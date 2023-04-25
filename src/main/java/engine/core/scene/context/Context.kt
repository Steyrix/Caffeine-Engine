package engine.core.scene.context

import java.awt.Window

interface Context {

    fun init(bundle: Bundle?)

    fun input(window: Window)

    fun render()

    fun update(deltaTime: Float)
}