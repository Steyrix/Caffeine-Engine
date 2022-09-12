package engine.core.update

sealed class SetOfParameters

data class SetOf2DParameters(
        var x: Float,
        var y: Float,
        var xSize: Float,
        var ySize: Float,
        var rotationAngle: Float
) : SetOfParameters()