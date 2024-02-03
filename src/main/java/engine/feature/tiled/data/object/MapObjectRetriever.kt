package engine.feature.tiled.data.`object`

import engine.feature.tiled.data.TileMap

object MapObjectRetriever {

    fun getObjectsAsEntities(
        tileMap: TileMap
    ): List<MapObjectEntity> {
        val objects = tileMap.retrieveObjects()

        return objects.map {
            MapObjectEntity(it).apply {
                level = tileMap.getTilePosition(it.tileIndices.maxOrNull() ?: 0).y + tileMap.getTileHeight()
            }
        }
    }
}