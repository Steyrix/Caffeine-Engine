package engine.feature.tiled.data.`object`

import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileMap

object MapObjectRetriever {

    fun getObjectsAsEntities(
        tileMap: TileMap,
        parameters: SetOfStatic2DParameters
    ): List<MapObjectEntity> {
        val objects = tileMap.retrieveObjects()

        return objects.map {
            MapObjectEntity(it, parameters)
        }
    }
}