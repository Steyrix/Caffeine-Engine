package engine.feature.tiled.data.`object`

import engine.core.game_object.SingleGameEntity
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.layer.LayerObject

class MapObjectEntity(
    private val layerObject: LayerObject,
    parameters: SetOfStatic2DParameters
) : SingleGameEntity() {

    init {
        it = layerObject
    }

    override fun getZLevel(): Float {
        return layerObject.graphicalComponent.y + 100
    }
}