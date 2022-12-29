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

    override fun execute(deltaTime: Float, params: SetOfParameters) {

        val parameters = params as SetOf2DParametersWithVelocity

        behaviorParams.movementX += deltaTime * parameters.velocityX
        behaviorParams.movementY += deltaTime * parameters.velocityY

        parameters.x += behaviorParams.movementX
        parameters.y += behaviorParams.movementY

        if (abs(behaviorParams.movementX) >= horizontalCap) {
            behaviorParams.movementX = 0f
            parameters.velocityX *= -1
        }

        if (abs(behaviorParams.movementY) >= verticalCap) {
            behaviorParams.movementY = 0f
            parameters.velocityY *= -1
        }
    }
}