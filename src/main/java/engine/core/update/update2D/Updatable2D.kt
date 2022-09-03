package engine.core.update.update2D

import engine.core.update.SetOfParameters
import engine.core.update.Updatable

interface Updatable2D : Updatable {
    var x: Float
    var y: Float
    var xSize: Float
    var ySize: Float
    var rotationAngle: Float

    // Each component should use its own set of parameters to update itself and its inner components
    override fun update(parameters: SetOfParameters) {
        if (parameters is SetOf2DParameters) {
            x = parameters.x
            y = parameters.y
            xSize = parameters.xSize
            ySize = parameters.ySize
            rotationAngle = parameters.rotationAngle
        }
    }
}