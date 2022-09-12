package demo.labyrinth

import engine.core.controllable.Controllable
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParameters
import engine.core.update.Updatable
import engine.core.window.Window
import org.lwjgl.glfw.GLFW

private class PlayerController(
        private val params: SetOf2DParameters
): Controllable, Entity, Updatable {

    var velocityX = 0f
    var velocityY = 0f
    override fun input(window: Window) {
        if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            velocityY = 10f
        } else {
            velocityY = 0f
        }

        if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            velocityX = 10f
        } else {
            velocityX = 0f
        }
    }

    override fun update(deltaTime: Float) {
        params.x += velocityX * deltaTime
        params.y += velocityY * deltaTime
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