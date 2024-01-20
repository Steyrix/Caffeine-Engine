package demo.medieval_game.data

import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters

// TODO: use resource file
// TODO: divide
const val HUMANOID_SIZE_TO_MAP_RELATION = 0.128f

var HUMANOID_SIZE_X = 102.4f
var HUMANOID_SIZE_Y = 102.4f

var hpBarParameters1 = SetOfStatic2DParameters(
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
    ySize = HUMANOID_SIZE_Y + 60f,
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

const val HUMANOID_BOX_OFFSET = 11.596801f

const val HUMANOID_BOX_SIZE = 79.2064f

val campfireParameters = SetOfStatic2DParameters(
    800f, 800f, 100f, 100f, 0f
)

val woodenChestParameters = SetOfStatic2DParameters(
    300f, 300f, 125f, 125f, 0f
)

val ironChestParameters = SetOfStatic2DParameters(
    450f, 300f, 125f, 125f, 0f
)

val rustyIronChestParameters = SetOfStatic2DParameters(
    600f, 300f, 125f, 125f, 0f
)

val blueChestParameters = SetOfStatic2DParameters(
    500f, 400f, 125f, 125f, 0f
)

val purpleChestParameters = SetOfStatic2DParameters(
    500f, 650f, 125f, 125f, 0f
)

val greenChestParameters = SetOfStatic2DParameters(
    500f, 800f, 125f, 125f, 0f
)

fun getMapParameters(screenWidth: Float, screenHeight: Float): SetOfStatic2DParameters {
    return SetOfStatic2DParameters(
        x = 0f, y = 0f, xSize = screenWidth, ySize = screenHeight, rotationAngle = 0f
    )
}