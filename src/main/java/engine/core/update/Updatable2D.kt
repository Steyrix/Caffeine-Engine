package engine.core.update

interface Updatable2D {
    var x: Float
    var y: Float
    var xSize: Float
    var ySize: Float
    var rotationAngle: Float

    fun update(
            x: Float,
            y: Float,
            xSize: Float,
            ySize: Float,
            rotationAngle: Float
    ) {
        this.x = x
        this.y = x
        this.xSize = xSize
        this.ySize = ySize
        this.rotationAngle = rotationAngle
    }
}