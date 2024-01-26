package engine.feature.tiled.data

import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.texture.Texture2D

class LayerObject(
    val tileIndices: List<Int>,
    texture2D: Texture2D,
    uv: FloatArray
) : CompositeEntity() {

    val graphicalComponent = Model(texture2D, uv)

    var isTransparent = false

}