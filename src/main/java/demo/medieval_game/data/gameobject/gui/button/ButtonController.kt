package demo.medieval_game.data.gameobject.gui.button

import engine.core.controllable.Controllable
import engine.core.entity.Entity
import engine.core.update.SetOfStatic2DParameters
import engine.core.window.Window

internal class ButtonController(
    var parameters: SetOfStatic2DParameters,
    val onClick: (Any?) -> Unit
) : Entity, Controllable {

    override fun input(window: Window) {
        val cursorPos = window.getCursorPosition()
        println("cursorPos: $cursorPos")
    }
}