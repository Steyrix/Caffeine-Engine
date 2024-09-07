package engine.feature.procedural.`object`

import engine.feature.procedural.MapElementType
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.data.layer.ObjectsLayer
import java.awt.geom.Point2D

interface GeneratedTiledObject {

    val type: MapElementType
    val tileSet: TileSet

    /**
     * Should be sorted
     */
    val tileIdsInSet: List<Int>

    fun List<GeneratedTiledObject>.toObjecstLayer(): ObjectsLayer {
        TODO()
    }

    /**
     * Method for determining positions' indices of object tiles on tile map
     */
    fun getObjectTilesPositions(startPos: Point2D): List<Int> {
        TODO()
    }
}