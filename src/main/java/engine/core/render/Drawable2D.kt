package engine.core.render

import engine.core.shader.Shader
import engine.core.update.Updatable2D

interface Drawable2D : Updatable2D {

    var shader: Shader?
    fun draw2D(
            x: Float,
            y: Float,
            xSize: Float,
            ySize: Float,
            rotationAngle: Float
    )
}