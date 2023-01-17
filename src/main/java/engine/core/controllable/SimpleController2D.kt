package engine.core.controllable

import demo.labyrinth.data.AnimationKey
import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.Updatable
import engine.core.window.Window
import org.lwjgl.glfw.GLFW

// TODO make reusable
class SimpleController2D(
        private val params: SetOf2DParametersWithVelocity,
        private var absVelocityY: Float = 0f,
        private var absVelocityX: Float = 0f,
        private var modifier: Float = 20f,
        private var isControlledByUser: Boolean = false
) : Controllable, Entity, Updatable {

    private var isWalking = false
    private var isJumping = false
    private var isDirectionRight = true
    private var isStriking = false

    private val strikeTimeLimit = 0.3f
    private var strikeAccumulatedTime = 0f

    override fun input(window: Window) {
        if (!isControlledByUser) return

        if (window.isKeyPressed(GLFW.GLFW_KEY_F) && !isStriking) {
            isStriking = true
        }

        params.velocityY = when {
            window.isKeyPressed(GLFW.GLFW_KEY_S) -> absVelocityY
            window.isKeyPressed(GLFW.GLFW_KEY_W) -> -absVelocityY
            else -> 0f
        }

        params.velocityX = when {
            window.isKeyPressed(GLFW.GLFW_KEY_D) -> {
                isDirectionRight = true
                absVelocityX
            }
            window.isKeyPressed(GLFW.GLFW_KEY_A) -> {
                isDirectionRight = false
                -absVelocityX
            }
            else -> 0f
        }
    }

    override fun update(deltaTime: Float) {
        if (isStriking) {
            strikeAccumulatedTime += deltaTime

            if (strikeAccumulatedTime >= strikeTimeLimit) {
                isStriking = false
                strikeAccumulatedTime = 0f
            }
        } else {
            params.x += params.velocityX * deltaTime * modifier
            params.y += params.velocityY * deltaTime * modifier
        }
        processState()
    }

    private fun processState() {
        if (isStriking) {
            isWalking = false
            isJumping = false
            params.velocityX = 0f
            params.velocityY = 0f
            return
        }

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

    // todo: remove coupling
    fun getAnimationKey(): String {
        return when {
            isStriking && isDirectionRight -> AnimationKey.STRIKE_R
            isStriking && !isDirectionRight -> AnimationKey.STRIKE_L
            isJumping && isDirectionRight -> AnimationKey.JUMP_R
            isJumping && !isDirectionRight -> AnimationKey.JUMP_L
            isWalking && isDirectionRight -> AnimationKey.WALK_R
            isWalking && !isDirectionRight -> AnimationKey.WALK_L
            isDirectionRight -> AnimationKey.IDLE_R
            else -> AnimationKey.IDLE_L
        }
    }
}