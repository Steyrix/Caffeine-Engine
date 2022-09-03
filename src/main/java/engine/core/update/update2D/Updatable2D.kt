package engine.core.update.update2D

interface Updatable2D {
    var x: Float
    var y: Float
    var xSize: Float
    var ySize: Float
    var rotationAngle: Float

    // Each component should use its own set of parameters to update itself and its inner components
    fun update(parameters: SetOf2DParameters) {
        x = parameters.x
        y = parameters.y
        xSize = parameters.xSize
        ySize = parameters.ySize
        rotationAngle = parameters.rotationAngle
    }
}