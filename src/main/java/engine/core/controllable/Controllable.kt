package engine.core.controllable

import engine.core.window.Window

interface Controllable {

    fun input(window: Window)
}