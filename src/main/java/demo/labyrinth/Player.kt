package demo.labyrinth

import engine.core.controllable.Controllable
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.Updatable
import engine.core.window.Window
import org.lwjgl.glfw.GLFW

private class PlayerController(
        private val params: SetOf2DParametersWithVelocity
): Controllable, Entity, Updatable {

    var modifier = 20f

    var isWalking = false
    var isJumping = false
    override fun input(window: Window) {
        params.velocityY = when {
            window.isKeyPressed(GLFW.GLFW_KEY_S) -> 10f
            window.isKeyPressed(GLFW.GLFW_KEY_W) -> -10f
            else -> 0f
        }

        params.velocityX = when {
            window.isKeyPressed(GLFW.GLFW_KEY_D) -> 10f
            window.isKeyPressed(GLFW.GLFW_KEY_A) -> -10f
            else -> 0f
        }
    }

    override fun update(deltaTime: Float) {
        params.x += params.velocityX * deltaTime * modifier
        params.y += params.velocityY * deltaTime * modifier
        processState()
    }

    private fun processState() {
        if (params.velocityX != 0f && !isWalking) {
            isWalking = true
        }

        if (params.velocityX == 0f) {
            isWalking = false
        }

        if (params.velocityY != 0f && !isJumping) {
            isJumping = true
        }

        if (params.velocityY == 0f) {
            isJumping = false
        }
    }
    fun getAnimationKey(): String {
        return if (isWalking || isJumping) {
            if (isJumping) AnimationKey.JUMP
            else AnimationKey.WALK
        } else {
            AnimationKey.IDLE
        }
    }
}

class Player(
        drawableComponent: AnimatedObject2D,
        params: SetOf2DParametersWithVelocity
) : CompositeEntity() {



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

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        components
                .filter { it.key is AnimatedObject2D }
                .forEach {
                    (it.key as AnimatedObject2D).setAnimationByKey(controller.getAnimationKey())
                }
    }
}