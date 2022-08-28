package engine.core.render

import engine.core.shader.Shader
import engine.core.update.Updatable2D

interface Drawable2D : Updatable2D {

    var shader: Shader?

    val innerDrawableComponents: MutableList<Drawable2D>
    fun draw2D() {
        innerDrawableComponents.forEach {
            it.draw2D()
        }
    }
}