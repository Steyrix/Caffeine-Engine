package engine.feature.tiled.data

import engine.core.entity.CompositeEntity
import engine.core.render.Model

class LayerObject(
    val tileIndices: Set<Int>,
    private val graphicalComponent: Model,
    private val transparencyUniform: String
) : CompositeEntity() {

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
}