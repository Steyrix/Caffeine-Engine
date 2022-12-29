package engine.core.entity.behavior

import engine.core.update.Behavior2DParameters
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfParameters
import kotlin.math.abs

class ConstrainedBehavior2D(
        private var horizontalCap: Float,
        private var verticalCap: Float,
        private var behaviorParams: Behavior2DParameters
) : Behavior {

    private var accumulatedMovementX = 0f
    private var accumulatedMovementY = 0f
    override fun execute(deltaTime: Float, params: SetOfParameters) {

        val parameters = params as SetOf2DParametersWithVelocity

        behaviorParams.movementX = deltaTime * parameters.velocityX
        behaviorParams.movementY = deltaTime * parameters.velocityY

        accumulatedMovementX += behaviorParams.movementX
        accumulatedMovementY += behaviorParams.movementY

        if (abs(accumulatedMovementX) >= horizontalCap) {
            accumulatedMovementX = 0f
            parameters.velocityX *= -1
        }

        if (abs(accumulatedMovementY) >= verticalCap) {
            accumulatedMovementY = 0f
            parameters.velocityY *= -1
        }
    }
}