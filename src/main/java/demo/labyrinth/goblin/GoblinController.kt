package demo.labyrinth.goblin

import demo.labyrinth.data.AnimationKey
import engine.core.controllable.Controllable
import engine.core.controllable.Direction
import engine.core.entity.Entity
import engine.core.loop.PredicateTimeEvent
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfParameters
import engine.core.update.Updatable
import engine.core.update.getCenterPoint
import engine.core.window.Window
import engine.feature.geometry.Point2D
import kotlin.math.abs

class GoblinController(
        private val params: SetOf2DParametersWithVelocity,
        private var modifier: Float = 20f,
) : Controllable, Entity, Updatable {

    var isStriking = false
    private var isWalking = false
    private var direction = Direction.RIGHT

    private val playStrikingAnimation = PredicateTimeEvent(
            timeLimit = 0.5f,
            predicate = { isStriking },
            action = { isStriking = false }
    )

    private var isStrikeCooldown = false

    private val strikeCooldown = PredicateTimeEvent(
            timeLimit = 1f,
            predicate = { isStrikeCooldown },
            action = { isStrikeCooldown = false }
    )

    override fun input(window: Window) {}

    override fun update(deltaTime: Float) {
        playStrikingAnimation.schedule(deltaTime)
        strikeCooldown.schedule(deltaTime)

        if (isStriking) return

        params.x += params.velocityX * deltaTime * modifier
        params.y += params.velocityY * deltaTime * modifier
        processState()
    }

    private fun processState() {
        if (isStriking) {
            isWalking = false
            return
        }

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

    fun strike(targetParams: SetOfParameters) {
        if (!isStrikeCooldown) {
            isStriking = true
            isStrikeCooldown = true
            isWalking = false
            directToTarget(targetParams)
        }
    }

    fun getCurrentCenterPos() = Point2D(params.x, params.y)

    fun getAnimationKey(): String {
        return when {
            isStriking -> getStrikingAnimation()
            isWalking -> getWalkingAnimation()
            else -> getIdleAnimation()
        }
    }

    private fun directToTarget(targetParams: SetOfParameters) {
        val targetCenter = targetParams.getCenterPoint()
        val thisCenter = params.getCenterPoint()

        val horizontalDirection = if (targetCenter.x <= thisCenter.x) {
            Direction.LEFT
        } else {
            Direction.RIGHT
        }

        val verticalDirection = if (targetCenter.y <= thisCenter.y) {
            Direction.UP
        } else {
            Direction.DOWN
        }

        direction = if (abs(targetCenter.x - thisCenter.x) >= abs(targetCenter.y - thisCenter.y)) {
            horizontalDirection
        } else {
            verticalDirection
        }
    }

    private fun getStrikingAnimation(): String {
        return when (direction) {
            Direction.RIGHT -> AnimationKey.GOBLIN_STRIKE_R
            Direction.LEFT -> AnimationKey.GOBLIN_STRIKE_L
            Direction.UP -> AnimationKey.GOBLIN_STRIKE_U
            Direction.DOWN -> AnimationKey.GOBLIN_STRIKE_D
        }
    }

    private fun getWalkingAnimation(): String {
        return when (direction) {
            Direction.RIGHT -> AnimationKey.GOBLIN_WALK_R
            Direction.LEFT -> AnimationKey.GOBLIN_WALK_L
            Direction.UP -> AnimationKey.GOBLIN_WALK_U
            Direction.DOWN -> AnimationKey.GOBLIN_WALK_D
        }
    }

    private fun getIdleAnimation(): String {
        return when (direction) {
            Direction.RIGHT -> AnimationKey.GOBLIN_IDLE_R
            Direction.LEFT -> AnimationKey.GOBLIN_IDLE_L
            Direction.UP -> AnimationKey.GOBLIN_IDLE_U
            Direction.DOWN -> AnimationKey.GOBLIN_IDLE_D
        }
    }
}