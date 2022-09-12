package demo.labyrinth

import engine.core.controllable.Controllable
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.update.update2D.SetOf2DParameters
import engine.core.window.Window
import org.lwjgl.glfw.GLFW

private class PlayerController(
        private val params: SetOf2DParameters
): Controllable, Entity {
    override fun input(window: Window) {
        if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            params.y += 10f
        }
    }
}

class Player(
        drawableComponent: AnimatedObject2D
) : CompositeEntity() {

    private val params = SetOf2DParameters(
            x = 0f,
            y = 0f,
            xSize = 50f,
            ySize = 50f,
            rotationAngle = 0f
    )

    private val controller = PlayerController(params)

    init {
        addComponent(
                component = drawableComponent,
                parameters = params
        )

        addComponent(
                component = controller,
                parameters = params
        )
    }
}