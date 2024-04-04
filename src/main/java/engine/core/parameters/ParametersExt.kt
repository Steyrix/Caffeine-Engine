package engine.core.parameters

import engine.core.geometry.Point2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfParameters
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

fun Point2D.isInBounds(params: SetOfParameters): Boolean {
    return (this.x <= params.xSize && x >= params.x) && (this.y <= params.ySize && y >= params.y)
}