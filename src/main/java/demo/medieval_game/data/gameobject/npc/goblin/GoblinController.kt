package demo.medieval_game.data.gameobject.npc.goblin

import demo.medieval_game.data.gameobject.npc.HumanoidAnimationController
import demo.medieval_game.interaction.IsAttackableInteraction
import engine.core.controllable.Direction
import engine.core.entity.Entity
import engine.core.loop.PredicateTimeEvent
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfParameters
import engine.core.update.Updatable
import engine.core.update.getCenterPoint
import engine.feature.geometry.Point2D
import engine.feature.interaction.Interaction
import kotlin.math.abs

class GoblinController(
        private val params: SetOf2DParametersWithVelocity,
        private var modifier: Float = 20f
) : HumanoidAnimationController(
        GoblinAnimationMaps.getIdleMap(),
        GoblinAnimationMaps.getStrikeMap(),
        GoblinAnimationMaps.getWalkMap()
), Entity, Updatable {

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

    override fun consumeInteraction(interaction: Interaction) {
        when(interaction) {
            is IsAttackableInteraction -> strike(interaction.targetParams)
        }
    }

    fun strike(targetParams: SetOfParameters) {
        if (!isStrikeCooldown && !isStriking) {
            isStriking = true
            isStrikeCooldown = true
            isWalking = false
            directToTarget(targetParams)
        }
    }

    fun getCurrentCenterPos() = Point2D(params.x, params.y)

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
}