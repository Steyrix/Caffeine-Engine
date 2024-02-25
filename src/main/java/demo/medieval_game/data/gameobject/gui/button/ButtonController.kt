package demo.medieval_game.data.gameobject.gui.button

import engine.core.controllable.Controllable
import engine.core.entity.Entity
import engine.core.update.SetOfStatic2DParameters
import engine.core.window.Window
import org.lwjgl.glfw.GLFW

internal class ButtonController(
    var buttonName: String = "",
    var parameters: SetOfStatic2DParameters,
    var onClick: (Any?) -> Unit,
    val onHover: (Boolean) -> Unit
) : Entity, Controllable {

    // TODO: offset between top bar and windows top border is making cursor height invalid
    override fun input(window: Window) {
        val cursorPos = window.getCursorPosition()

        if (isIntersecting(cursorPos.x, cursorPos.y)) {
            if (window.isKeyPressed(GLFW.GLFW_KEY_1)) {
                onClick.invoke("")
            }
            onHover.invoke(true)
        } else {
            onHover.invoke(false)
        }
    }

    private fun isIntersecting(x: Float, y: Float) = isIntersectingHorizontally(x) && isIntersectingVertically(y)

    private fun isIntersectingHorizontally(x: Float) = x >= parameters.x && x <= parameters.x + parameters.xSize
    private fun isIntersectingVertically(y: Float) = y >= parameters.y && y <= parameters.y + parameters.ySize
}