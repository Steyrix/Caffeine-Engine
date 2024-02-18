package demo.medieval_game.data.gameobject.gui.button

import demo.medieval_game.matrix.MedievalGameMatrixState
import demo.medieval_game.scene.MedievalGame
import engine.core.controllable.Controllable
import engine.core.entity.Entity
import engine.core.update.SetOfStatic2DParameters
import engine.core.window.Window
import org.joml.Matrix4f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW

internal class ButtonController(
    var parameters: SetOfStatic2DParameters,
    var onClick: (Any?) -> Unit,
    val onHover: (Boolean) -> Unit
) : Entity, Controllable {

    override fun input(window: Window) {
        val cursorPos = window.getCursorPosition()

        val xNdc = cursorPos.x / MedievalGame.screenWidth * 2 - 1
        val yNdc = -1 * cursorPos.y / MedievalGame.screenHeight * 2 + 1

        val vecNdc = Vector4f(xNdc, yNdc, -1f, 1f)
        val mat = Matrix4f()
        MedievalGame.renderProjection.invert(mat)
        val vecEye = vecNdc.mul(mat)

        val x = vecEye.x * vecEye.w
        val y = vecEye.y * vecEye.w - MedievalGameMatrixState.worldTranslation.y / 2

        if (isIntersecting(x, y)) {
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