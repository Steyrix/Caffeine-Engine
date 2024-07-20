package engine.feature.tiled.data.layer

import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.TileSet

typealias MapData = MutableList<Pair<Point2D, Int>>

object ProceduralGenerator {

    fun createTileMap(
        data: Map<TileSet, MapData>
    ): TileMap {

        val graphicalLayers = mutableListOf<Model>()

        data.keys.forEach {

        }
        TODO()
    }
}