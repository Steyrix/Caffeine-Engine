package engine.feature.tiled.traversing

import engine.core.entity.Entity
import engine.feature.tiled.TileMap

class TileTraverser(
        tileGraph: Map<Int, MutableList<Int>>,
        entity: Entity
) {

    fun moveToTile(
            map: TileMap,
            tileIndex: Int
    ) {

    }

    fun getShortestPath(): List<Int> {
        // djikstra
        return emptyList()
    }
}