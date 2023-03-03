package demo.labyrinth.data

import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters

var hpBarPatameters1 = SetOfStatic2DParameters(
        x = 0f,
        y = 0f,
        xSize = 75f,
        ySize = 25f,
        rotationAngle = 0f
)

var hpBarPatameters2 = SetOfStatic2DParameters(
        x = 0f,
        y = 0f,
        xSize = 75f,
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
                velocityX = 0f,
                velocityY = 0f
        ),
        SetOf2DParametersWithVelocity(
                x = 250f,
                y = 120f,
                xSize = 50f,
                ySize = 50f,
                rotationAngle = 0f,
                velocityX = 0f,
                velocityY = 0f
        )
)