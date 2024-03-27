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
) : SetOfParameters {

    companion object {
        fun createEmpty(): SetOfStatic2DParameters {
            return SetOfStatic2DParameters(
                x = 0f,
                y = 0f,
                xSize = 0f,
                ySize = 0f,
                rotationAngle = 0f
            )
        }
    }
}

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