package engine.core.render

import engine.core.update.update2D.Parameterized

interface Drawable2D : Drawable, Parameterized {

    val innerDrawableComponents: MutableList<Drawable2D>
    override fun draw() {
        innerDrawableComponents.forEach {
            it.draw()
        }
    }
}