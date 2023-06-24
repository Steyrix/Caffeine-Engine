package engine.core.render

import engine.core.shader.Shader
import engine.core.update.update2D.Parameterized

interface Drawable2D : Drawable, Parameterized {

    var shader: Shader?

    val innerDrawableComponents: MutableList<Drawable2D>
    override fun draw() {
        innerDrawableComponents.forEach {
            it.draw()
        }
    }
}