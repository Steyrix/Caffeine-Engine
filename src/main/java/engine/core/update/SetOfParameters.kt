package engine.core.update

import engine.core.geometry.Point2D

sealed interface SetOfParameters {
    var x: Float
    var y: Float
    var xSize: Float
    var ySize: Float
    var rotationAngle: Float
}

data class SetOfStatic2DParameters(
    override var x: Float = 0f,
    override var y: Float = 0f,
    override var xSize: Float = 0f,
    override var ySize: Float = 0f,
    override var rotationAngle: Float = 0f,
) : SetOfParameters

data class SetOfStatic2DParametersWithOffset(
    override var x: Float = 0f,
    override var y: Float = 0f,
    override var xSize: Float = 0f,
    override var ySize: Float = 0f,
    override var rotationAngle: Float = 0f,
    var xOffset: Float = 0f,
    var yOffset: Float = 0f
) : SetOfParameters

fun SetOfParameters.getCenterPoint(): Point2D {
    return Point2D(
        x + xSize / 2,
        y + ySize / 2
    )
}