package engine.core.render

import engine.core.shader.Shader

interface Drawable {

    var shader: Shader?

    fun draw()
}