package engine.core.parameters

import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters

fun SetOf2DParametersWithVelocity.toStatic(): SetOfStatic2DParameters {
    return SetOfStatic2DParameters(
        x,
        y,
        xSize,
        ySize,
        rotationAngle
    )
}