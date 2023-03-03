package demo.labyrinth

import demo.labyrinth.data.AnimationKey
import engine.core.controllable.Controllable
import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.Updatable
import engine.core.window.Window
import org.lwjgl.glfw.GLFW

class SimpleController2D(
        private val params: SetOf2DParametersWithVelocity,
        private var absVelocityY: Float = 0f,
        private var absVelocityX: Float = 0f,
        private var modifier: Float = 20f,
        private var isControlledByUser: Boolean = false
) : Controllable, Entity, Updatable {

    enum class Direction {
        RIGHT,
        LEFT,
        UP,
        DOWN
    }

    private var isWalking = false
    private var direction = Direction.RIGHT

    override fun input(window: Window) {
        if (!isControlledByUser) return

        params.velocityY = when {
            window.isKeyPressed(GLFW.GLFW_KEY_S) -> {
                direction = Direction.DOWN
                absVelocityY
            }
            window.isKeyPressed(GLFW.GLFW_KEY_W) -> {
                direction = Direction.UP
                -absVelocityY
            }
            else -> 0f
        }

        params.velocityX = when {
            window.isKeyPressed(GLFW.GLFW_KEY_D) -> {
                direction = Direction.RIGHT
                absVelocityX
            }
            window.isKeyPressed(GLFW.GLFW_KEY_A) -> {
                direction = Direction.LEFT
                -absVelocityX
            }
            else -> 0f
        }
    }

    override fun update(deltaTime: Float) {
        params.x += params.velocityX * deltaTime * modifier
        params.y += params.velocityY * deltaTime * modifier
        processState()
    }

    private fun processState() {
        if ((params.velocityX != 0f || params.velocityY != 0f) && !isWalking) {
            isWalking = true
        }

        if (params.velocityY == 0f && params.velocityX == 0f) {
            isWalking = false
        }
    }

    fun getAnimationKey(): String {
        return if (isWalking) {
            when (direction) {
                Direction.RIGHT -> AnimationKey.WALK_R
                Direction.LEFT -> AnimationKey.WALK_L
                Direction.UP -> AnimationKey.WALK_U
                Direction.DOWN -> AnimationKey.WALK_D
            }
        } else when (direction) {
            Direction.RIGHT -> AnimationKey.IDLE_R
            Direction.LEFT -> AnimationKey.IDLE_L
            Direction.UP -> AnimationKey.IDLE_U
            Direction.DOWN -> AnimationKey.IDLE_D
        }
    }
}