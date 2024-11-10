package engine.core.render

import engine.core.shader.Shader
import engine.core.update.SetOfParameters
import engine.core.update.update2D.Parameterized

interface Drawable<P : SetOfParameters> : Parameterized<P>, Zleveled {

    var shader: Shader?

    override var zLevel: Float

    fun draw()
}