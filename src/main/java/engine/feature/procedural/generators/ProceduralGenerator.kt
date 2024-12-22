package engine.feature.procedural.generators

import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.data.layer.Layer
import engine.feature.tiled.data.layer.TileLayer

object ProceduralGenerator {

    fun generateMap(
        widthInTiles: Int,
        heightInTiles: Int,
        numSeeds: Int,
        biomeMap: Map<String, TileSet>,
        seed: Int,
    ): TileMap {
        val voronoiMapData =
            VoronoiBasedGenerator.generate(
                widthInTiles,
                heightInTiles,
                numSeeds,
                biomeMap.map { it.key },
                seed
            )

        var index = 0
        val tileLayers = biomeMap.map {
            generateLayers(
                index++,
                widthInTiles,
                heightInTiles,
                targetBiome = it.key,
                targetSet = it.value,
                voronoiMapData
            )
        }

        return TileMap(
            layers = tileLayers,
            widthInTiles = widthInTiles,
            heightInTiles = heightInTiles
        )
    }

    private fun generateLayers(
        layerIndex: Int,
        widthInTiles: Int,
        heightInTiles: Int,
        targetBiome: String,
        targetSet: TileSet,
        voronoiMapData: Array<Array<String>>
    ): Layer {

        val tileIds = mutableListOf<Int>()
        for (i in 0 .. widthInTiles) {
            for (j in 0 .. heightInTiles) {
                if (voronoiMapData[i][j] != targetBiome) {
                    tileIds.add(-1)
                } else {
                    val setOfIds = (0 until targetSet.getUniqueTilesCount()).toList()
                    tileIds.add(setOfIds.random())
                }
            }
        }

        return TileLayer(
            name = "walkable_layer_$layerIndex",
            widthInTiles = widthInTiles,
            heightInTiles = heightInTiles,
            set = targetSet,
            tileIdsData = tileIds,
            properties = mutableListOf(),
            model = null
        )
    }
}