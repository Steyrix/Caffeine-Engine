package demo.medieval_game.data.starting_level

import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters

const val HUMANOID_SIZE_TO_MAP_RELATION = 0.128f

const val HUMANOID_BOX_OFFSET_MODIFIER = 0.2265f

var HUMANOID_SIZE_X = 128f
var HUMANOID_SIZE_Y = 128f

var hpBarPatameters1 = SetOfStatic2DParameters(
        x = 0f,
        y = 0f,
        xSize = 50f,
        ySize = 12.5f,
        rotationAngle = 0f
)

var characterParameters = SetOf2DParametersWithVelocity(
        x = 500f,
        y = 500f,
        xSize = HUMANOID_SIZE_X,
        ySize = HUMANOID_SIZE_Y,
        rotationAngle = 0f,
        velocityX = 0f,
        velocityY = 0f
)

val goblinParams1 = SetOf2DParametersWithVelocity(
        x = 150f,
        y = 120f,
        xSize = HUMANOID_SIZE_X,
        ySize = HUMANOID_SIZE_Y,
        rotationAngle = 0f,
        velocityX = 0f,
        velocityY = 0f
)

val goblinParams2 = SetOf2DParametersWithVelocity(
        x = 250f,
        y = 120f,
        xSize = HUMANOID_SIZE_X,
        ySize = HUMANOID_SIZE_Y,
        rotationAngle = 0f,
        velocityX = 0f,
        velocityY = 0f
)

fun getHumanoidBoxOffset() = HUMANOID_SIZE_X * HUMANOID_BOX_OFFSET_MODIFIER / 2

fun getHumanoidBoxSize() = HUMANOID_SIZE_X - getHumanoidBoxOffset() * 2

val campfireParameters: SetOfStatic2DParameters = SetOfStatic2DParameters(
        800f, 800f, 100f, 100f, 0f
)

fun getMapParameters(screenWidth: Float, screenHeight:Float): SetOfStatic2DParameters {
    return SetOfStatic2DParameters(
            x = 0f, y = 0f, xSize = screenWidth, ySize = screenHeight, rotationAngle = 0f
    )
}

fun recalculateParameters(xSize: Float, ySize: Float) {
    HUMANOID_SIZE_X = HUMANOID_SIZE_TO_MAP_RELATION * xSize
    HUMANOID_SIZE_Y = HUMANOID_SIZE_TO_MAP_RELATION * ySize

    characterParameters.xSize = HUMANOID_SIZE_X
    characterParameters.ySize = HUMANOID_SIZE_Y
    goblinParams1.xSize = HUMANOID_SIZE_X
    goblinParams1.ySize = HUMANOID_SIZE_Y
    goblinParams2.xSize = HUMANOID_SIZE_X
    goblinParams2.ySize = HUMANOID_SIZE_Y

}