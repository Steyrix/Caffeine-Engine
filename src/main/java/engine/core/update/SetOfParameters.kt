package engine.core.update

import engine.feature.geometry.Point2D

interface SetOfParameters {
    var x: Float
    var y: Float
    var xSize: Float
    var ySize: Float
    var rotationAngle: Float
}

class SetOfStatic2DParameters(
        override var x: Float,
        override var y: Float,
        override var xSize: Float,
        override var ySize: Float,
        override var rotationAngle: Float,
) : SetOfParameters

class SetOf2DParametersWithVelocity(
        override var x: Float,
        override var y: Float,
        override var xSize: Float,
        override var ySize: Float,
        override var rotationAngle: Float,
        var velocityX: Float,
        var velocityY: Float
) : SetOfParameters

class Behavior2DParameters(
        var movementX: Float,
        var movementY: Float
)

fun SetOf2DParametersWithVelocity.getCenterPoint(): Point2D {
    return Point2D(
         x + xSize / 2,
            y + ySize / 2
    )
}