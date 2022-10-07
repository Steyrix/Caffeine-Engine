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

    var modifier = 20f

    var velocityX = 0f
    var velocityY = 0f

    var isWalking = false
    var isJumping = false
    override fun input(window: Window) {
        velocityY = if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            10f
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            -10f
        } else {
            0f
        }

        velocityX = if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            10f
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            -10f
        } else {
            0f
        }
    }

    override fun update(deltaTime: Float) {
        params.x += velocityX * deltaTime * modifier
        params.y += velocityY * deltaTime * modifier
        processState()
    }

    private fun processState() {
        if (velocityX != 0f && !isWalking) {
            isWalking = true
        }

        if (velocityX == 0f) {
            isWalking = false
        }

        if (velocityY != 0f && !isJumping) {
            isJumping = true
        }

        if (velocityY == 0f) {
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

    fun dropVelocity() {
        println("DROP")
        if (velocityX != 0f) {
            params.x -= velocityX * modifier * 0.05f
            velocityX = 0f
        }
        if (velocityY != 0f) velocityY = 0f
    }
}

class Player(
        drawableComponent: AnimatedObject2D,
        private val params: SetOf2DParameters
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

    fun collide() {
        controller.dropVelocity()
    }
}