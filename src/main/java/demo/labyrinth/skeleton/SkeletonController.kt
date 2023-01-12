package demo.labyrinth.skeleton

import demo.labyrinth.data.AnimationKey
import engine.core.controllable.Controllable
import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.Updatable
import engine.core.window.Window
import org.lwjgl.glfw.GLFW

class SkeletonController(
        private val params: SetOf2DParametersWithVelocity,
        private var modifier: Float = 20f,
) : Controllable, Entity, Updatable {

    private var isWalking = false
    private var isJumping = false
    private var isDirectionRight = true

    override fun input(window: Window) {}

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

        isDirectionRight = params.velocityX > 0f
    }

    fun getAnimationKey(): String {
        return when {
            isJumping && isDirectionRight -> AnimationKey.JUMP_R_SKEL
            isJumping && !isDirectionRight -> AnimationKey.JUMP_L_SKEL
            isWalking && isDirectionRight -> AnimationKey.WALK_R_SKEL
            isWalking && !isDirectionRight -> AnimationKey.WALK_L_SKEL
            isDirectionRight -> AnimationKey.IDLE_R_SKEL
            else -> AnimationKey.IDLE_L_SKEL
        }
    }
}