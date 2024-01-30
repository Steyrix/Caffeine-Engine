package engine.feature.tiled.data.layer

import engine.core.entity.CompositeEntity
import engine.core.render.Drawable
import engine.core.render.Model
import engine.core.shader.Shader
import engine.core.update.ParametersFactory
import engine.core.update.SetOfStatic2DParameters

class LayerObject(
    val tileIndices: Set<Int>,
    val graphicalComponent: Model,
    private val transparencyUniform: String
) : CompositeEntity() {

    var isTransparent = false

    var transparencyValue: Float = 0f

    var shader: Shader? = null
        set(value) {
            field = value
            graphicalComponent.shader = value
        }

    var zLevel: Float = Float.POSITIVE_INFINITY
        set(value) {
            field = value
            graphicalComponent.zLevel = value
        }

    init {
        addComponent(graphicalComponent, ParametersFactory.createEmptyStatic())
    }

    fun isIntersecting(tileIndex: Int): Boolean {
        return tileIndices.contains(tileIndex)
    }

    fun updateParameters(parameters: SetOfStatic2DParameters) {
        graphicalComponent.updateParameters(parameters)
        graphicalComponent.shader?.let {
            it.bind()
            it.setUniform(transparencyUniform, transparencyValue)
        }
    }

    fun updateMesh(bufferIndex: Int, offset: Long, data: FloatArray) {
        graphicalComponent.updateMesh(bufferIndex, offset, data)
    }
}