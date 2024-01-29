package engine.feature.tiled.data.layer

import engine.core.entity.CompositeEntity
import engine.core.render.Drawable
import engine.core.render.Model
import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters

class LayerObject(
    val tileIndices: Set<Int>,
    private val graphicalComponent: Model,
    private val transparencyUniform: String
) : CompositeEntity(), Drawable<SetOfStatic2DParameters> {

    var isTransparent = false

    var transparencyValue: Float = 0f

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        graphicalComponent.shader?.let {
            it.bind()
            it.setUniform(transparencyUniform, transparencyValue)
        }
    }

    fun isIntersecting(tileIndex: Int): Boolean {
        return tileIndices.contains(tileIndex)
    }

    override var shader: Shader? = graphicalComponent.shader

    override var zLevel: Float = 0f

    override fun updateParameters(parameters: SetOfStatic2DParameters) {
        graphicalComponent.updateParameters(parameters)
    }

    fun updateMesh(bufferIndex: Int, offset: Long, data: FloatArray) {
        graphicalComponent.updateMesh(bufferIndex, offset, data)
    }
}