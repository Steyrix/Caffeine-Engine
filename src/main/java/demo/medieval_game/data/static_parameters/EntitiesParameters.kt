package demo.medieval_game.data.static_parameters

import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters

// TODO: use resource file
// TODO: divide
const val HUMANOID_SIZE_TO_MAP_RELATION = 0.128f

var HUMANOID_SIZE_X = 55f
var HUMANOID_SIZE_Y = 87f

var defaultHpBarParams = SetOfStatic2DParameters(
    x = 0f,
    y = 0f,
    xSize = 50f,
    ySize = 12.5f,
    rotationAngle = 0f
)

fun createDefaultHpBarParams(): SetOfStatic2DParameters {
    return SetOfStatic2DParameters(
        x = 0f,
        y = 0f,
        xSize = 50f,
        ySize = 12.5f,
        rotationAngle = 0f
    )
}

var characterParameters = SetOf2DParametersWithVelocity(
    x = 100f,
    y = 100f,
    xSize = HUMANOID_SIZE_X,
    ySize = HUMANOID_SIZE_Y,
    rotationAngle = 0f,
    velocityX = 0f,
    velocityY = 0f
)

val goblinParams1 = SetOf2DParametersWithVelocity(
    x = 150f,
    y = 120f,
    xSize = 76.2f,
    ySize = 76.2f,
    rotationAngle = 0f,
    velocityX = 0f,
    velocityY = 0f
)

val goblinParams2 = SetOf2DParametersWithVelocity(
    x = 250f,
    y = 120f,
    xSize = 76.2f,
    ySize = 76.2f,
    rotationAngle = 0f,
    velocityX = 0f,
    velocityY = 0f
)

const val HUMANOID_BOX_OFFSET_X = 18.59680f
const val HUMANOID_BOX_OFFSET_Y = 20f

const val HUMANOID_BOX_WIDTH = 65.2064f
const val HUMANOID_BOX_HEIGHT = 90f

val campfireParameters = SetOfStatic2DParameters(
    568f, 765f, 158f, 150f, 0f
)

val woodenChestParameters = SetOfStatic2DParameters(
    300f, 300f, 100f, 100f, 0f
)

val ironChestParameters = SetOfStatic2DParameters(
    450f, 300f, 100f, 100f, 0f
)

val rustyIronChestParameters = SetOfStatic2DParameters(
    600f, 300f, 100f, 100f, 0f
)

val blueChestParameters = SetOfStatic2DParameters(
    500f, 400f, 100f, 100f, 0f
)

val purpleChestParameters = SetOfStatic2DParameters(
    500f, 650f, 100f, 100f, 0f
)

val greenChestParameters = SetOfStatic2DParameters(
    500f, 800f, 100f, 100f, 0f
)

const val CHEST_BOX_WIDTH = 75f
const val CHEST_BOX_X_OFFSET = 25f
const val CHEST_BOX_HEIGHT = 75f
const val CHEST_BOX_Y_OFFSET = 25f

fun getMapParameters(screenWidth: Float, screenHeight: Float): SetOfStatic2DParameters {
    return SetOfStatic2DParameters(
        x = 0f, y = 0f, xSize = screenWidth, ySize = screenHeight, rotationAngle = 0f
    )
}