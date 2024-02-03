package engine.feature.tiled.data.`object`

import engine.core.game_object.DynamicGameEntity
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.layer.LayerObject

class MapObjectEntity(
    private val layerObject: LayerObject,
    parameters: SetOfStatic2DParameters
) : DynamicGameEntity<SetOfStatic2DParameters>(parameters) {

    init {
        it?.addComponent(layerObject, parameters)
    }

    override fun preSpawn(setOfParameters: SetOfStatic2DParameters) {

    }

    override fun getParams(): SetOfStatic2DParameters {
        return parameters.copy()
    }

    override fun getZLevel(): Float {
        return layerObject.graphicalComponent.y + layerObject.graphicalComponent.ySize
    }
}