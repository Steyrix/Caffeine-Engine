package demo.labyrinth.goblin

import demo.labyrinth.data.AnimationKey
import engine.core.controllable.Controllable
import engine.core.controllable.Direction
import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.Updatable
import engine.core.window.Window

class GoblinController(
        private val params: SetOf2DParametersWithVelocity,
        private var modifier: Float = 20f,
) : Controllable, Entity, Updatable {

    private var isWalking = false
    private var direction = Direction.RIGHT

    override fun input(window: Window) {}

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

        direction = when {
            params.velocityX > 0f -> Direction.RIGHT
            params.velocityX < 0f -> Direction.LEFT
            params.velocityY > 0f -> Direction.DOWN
            params.velocityY < 0f -> Direction.UP
            else -> direction
        }
    }

    fun getAnimationKey(): String {
        return if (isWalking) {
            when (direction) {
                Direction.RIGHT -> AnimationKey.GOBLIN_WALK_R
                Direction.LEFT -> AnimationKey.GOBLIN_WALK_L
                Direction.UP -> AnimationKey.GOBLIN_WALK_U
                Direction.DOWN -> AnimationKey.GOBLIN_WALK_D
            }
        } else when (direction) {
            Direction.RIGHT -> AnimationKey.GOBLIN_IDLE_R
            Direction.LEFT -> AnimationKey.GOBLIN_IDLE_L
            Direction.UP -> AnimationKey.GOBLIN_IDLE_U
            Direction.DOWN -> AnimationKey.GOBLIN_IDLE_D
        }
    }
}