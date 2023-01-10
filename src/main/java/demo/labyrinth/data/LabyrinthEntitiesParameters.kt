package demo.labyrinth.data

import engine.core.entity.behavior.ConstrainedBehavior2D
import engine.core.update.Behavior2DParameters
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters

var hpBarPatameters = SetOfStatic2DParameters(
        x = 0f,
        y = 0f,
        xSize = 50f,
        ySize = 25f,
        rotationAngle = 0f
)

var characterParameters = SetOf2DParametersWithVelocity(
        x = 30f,
        y = 30f,
        xSize = 50f,
        ySize = 50f,
        rotationAngle = 0f,
        velocityX = 0f,
        velocityY = 0f
)

val campfireParameters: SetOfStatic2DParameters = SetOfStatic2DParameters(
        900f, 750f, 50f, 50f, 0f
)

fun getMapParameters(screenWidth: Float, screenHeight:Float): SetOfStatic2DParameters {
    return SetOfStatic2DParameters(
            x = 0f, y = 0f, xSize = screenWidth, ySize = screenHeight, rotationAngle = 0f
    )
}

val crateParameters: SetOfStatic2DParameters = SetOfStatic2DParameters(
        x = 400f, y = 430f, xSize = 50f, ySize = 50f, rotationAngle = 0f
)

val skeletonParameters: List<SetOf2DParametersWithVelocity> = listOf(
        SetOf2DParametersWithVelocity(
                x = 150f,
                y = 120f,
                xSize = 50f,
                ySize = 50f,
                rotationAngle = 0f,
                velocityX = 10f,
                velocityY = 0f
        ),
        SetOf2DParametersWithVelocity(
                x = 250f,
                y = 120f,
                xSize = 50f,
                ySize = 50f,
                rotationAngle = 0f,
                velocityX = 10f,
                velocityY = 0f
        )
)

val skeletonBehavior1 = ConstrainedBehavior2D(
    horizontalCap = 20f,
        verticalCap = 200f,
        behaviorParams = Behavior2DParameters(
                movementX = 0f,
                movementY = 0f
        )
)

val skeletonBehavior2 = ConstrainedBehavior2D(
        horizontalCap = 20f,
        verticalCap = 200f,
        behaviorParams = Behavior2DParameters(
                movementX = 0f,
                movementY = 0f
        )
)

val skeletonBehaviors = listOf(skeletonBehavior1, skeletonBehavior2)