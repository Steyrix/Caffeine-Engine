package engine.core.render

import engine.core.shader.Shader

interface Drawable {

    var shader: Shader?
    fun draw(
            x: Float,
            y: Float,
            xSize: Float,
            ySize: Float,
            rotationAngle: Float,
            shader: Shader
    )
}