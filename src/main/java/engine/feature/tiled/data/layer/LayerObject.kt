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

    var transparencyValue: Float = 1f
        set(value) {
            graphicalComponent.shader?.let {
                it.bind()
                it.setUniform(transparencyUniform, value)
            }
        }

    var shader: Shader? = null
        set(value) {
            field = value
            graphicalComponent.shader = value
        }

    init {
        addComponent(graphicalComponent, ParametersFactory.createEmptyStatic())
    }

    override fun update(deltaTime: Float) {
        Unit //TODO: That's a hack
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