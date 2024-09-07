package engine.feature.procedural.`object`

import engine.feature.procedural.MapElementType
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.data.layer.ObjectsLayer

interface GeneratedTiledObject {

    val type: MapElementType
    val tileSet: TileSet
    val tileIds: List<Int>

    fun List<GeneratedTiledObject>.toObjecstLayer(): ObjectsLayer {
        TODO()
    }

    fun getObjectTilesPositions(): List<Int> {
        TODO()
    }
}