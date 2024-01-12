package engine.feature.tiled.data.debug

import engine.core.render.Drawable
import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters

class TileDebugModel : Drawable<SetOfStatic2DParameters> {

    override var shader: Shader? = null

    override var zLevel: Float = 0f
    override fun draw() {
        TODO("Not yet implemented")
    }

    override fun updateParameters(parameters: SetOfStatic2DParameters) {
        TODO("Not yet implemented")
    }
}