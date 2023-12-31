package engine.core.render

import engine.core.shader.Shader
import engine.core.update.SetOfParameters
import engine.core.update.update2D.Parameterized

interface Drawable<P : SetOfParameters> : Parameterized<P> {

    var shader: Shader?

    fun draw()

    fun getZlevel(): Float = 1f
}