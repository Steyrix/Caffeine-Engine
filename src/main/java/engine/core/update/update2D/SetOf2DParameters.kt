package engine.core.update.update2D

import engine.core.update.SetOfParameters

data class SetOf2DParameters(
        var x: Float,
        var y: Float,
        var xSize: Float,
        var ySize: Float,
        var rotationAngle: Float
) : SetOfParameters