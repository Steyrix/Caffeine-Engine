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
        override var x: Float,
        override var y: Float,
        override var xSize: Float,
        override var ySize: Float,
        override var rotationAngle: Float,
) : SetOfParameters

data class SetOf2DParametersWithVelocity(
        override var x: Float,
        override var y: Float,
        override var xSize: Float,
        override var ySize: Float,
        override var rotationAngle: Float,
        var velocityX: Float,
        var velocityY: Float
) : SetOfParameters

data class SetOfStatic2DParametersWithOffset(
        override var x: Float,
        override var y: Float,
        override var xSize: Float,
        override var ySize: Float,
        override var rotationAngle: Float,
        var xOffset: Float,
        var yOffset: Float
) : SetOfParameters

fun SetOfParameters.getCenterPoint(): Point2D {
    return Point2D(
            x + xSize / 2,
            y + ySize / 2
    )
}