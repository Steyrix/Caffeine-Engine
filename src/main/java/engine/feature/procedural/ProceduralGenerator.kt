package engine.feature.procedural

import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.data.layer.Layer
import engine.feature.tiled.data.layer.TileLayer
import engine.feature.tiled.data.layer.TileLayerInitializer

typealias MapData = MutableList<Pair<Point2D, Int>>

object ProceduralGenerator {

    fun createTileMap(
        data: Map<TileSet, MapData>,
        widthInTiles: Int,
        heightInTiles: Int
    ): TileMap {
        val graphicalLayers = mutableListOf<Pair<TileSet, Model>>()

        data.keys.forEach { set ->
            data[set]?.let { mapData ->
                graphicalLayers.add(
                    set to
                    TileLayerInitializer.genLayerModelByData(mapData, set)
                )
            }
        }

        val layers = createLayers(
            graphicalLayers,
            data,
            widthInTiles,
            heightInTiles
        )

        return TileMap(
            layers,
            widthInTiles = widthInTiles,
            heightInTiles = heightInTiles
        )
    }

    private fun createLayers(
        graphicalLayers: List<Pair<TileSet, Model>>,
        data: Map<TileSet, MapData>,
        widthInTiles: Int,
        heightInTiles: Int
    ): MutableList<Layer> {
        val layers: MutableList<Layer> = graphicalLayers.mapIndexed { index, it ->
            val tileIdsData = data[it.first]!!.map {
                it.second
            }.toMutableList()

            TileLayer(
                "generated_layer_$index",
                widthInTiles,
                heightInTiles,
                set = it.first,
                tileIdsData,
                model = it.second
            )
        }.toMutableList()

        return layers
    }
}