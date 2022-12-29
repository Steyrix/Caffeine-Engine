package engine.core.update

sealed class SetOfParameters

data class SetOfStatic2DParameters(
        var x: Float,
        var y: Float,
        var xSize: Float,
        var ySize: Float,
        var rotationAngle: Float
) : SetOfParameters()

data class SetOf2DParametersWithVelocity(
        var x: Float,
        var y: Float,
        var xSize: Float,
        var ySize: Float,
        var rotationAngle: Float,
        var velocityX: Float,
        var velocityY: Float
) : SetOfParameters()

data class Behavior2DParameters(
        var movementX: Float,
        var movementY: Float
) : SetOfParameters()