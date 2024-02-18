package demo.medieval_game.data.gameobject.gui.button

import engine.core.controllable.Controllable
import engine.core.entity.Entity
import engine.core.window.Window

class ButtonController(
    val onClick: (Any?) -> Unit
) : Entity, Controllable {

    override fun input(window: Window) {
        val cursorPos = window.getCursorPosition()
        println("cursorPos: $cursorPos")
    }
}