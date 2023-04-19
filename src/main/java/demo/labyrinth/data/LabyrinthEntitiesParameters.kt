package demo.labyrinth.data

import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters

var hpBarPatameters1 = SetOfStatic2DParameters(
        x = 0f,
        y = 0f,
        xSize = 50f,
        ySize = 12.5f,
        rotationAngle = 0f
)

var characterParameters = SetOf2DParametersWithVelocity(
        x = 30f,
        y = 30f,
        xSize = 128f,
        ySize = 128f,
        rotationAngle = 0f,
        velocityX = 0f,
        velocityY = 0f
)

const val HUMANIOD_BOX_OFFSET_MODIFIER = 0.2265f

fun getCharacterBoxOffset() = characterParameters.xSize * HUMANIOD_BOX_OFFSET_MODIFIER / 2

fun getCharacterBoxSize() = characterParameters.xSize - getCharacterBoxOffset() * 2

val campfireParameters: SetOfStatic2DParameters = SetOfStatic2DParameters(
        900f, 750f, 50f, 50f, 0f
)

fun getMapParameters(screenWidth: Float, screenHeight:Float): SetOfStatic2DParameters {
    return SetOfStatic2DParameters(
            x = 0f, y = 0f, xSize = screenWidth, ySize = screenHeight, rotationAngle = 0f
    )
}

val goblinParams1 = SetOf2DParametersWithVelocity(
        x = 150f,
        y = 120f,
        xSize = 128f,
        ySize = 128f,
        rotationAngle = 0f,
        velocityX = 0f,
        velocityY = 0f
)

val goblinParams2 = SetOf2DParametersWithVelocity(
        x = 250f,
        y = 120f,
        xSize = 128f,
        ySize = 128f,
        rotationAngle = 0f,
        velocityX = 0f,
        velocityY = 0f
)