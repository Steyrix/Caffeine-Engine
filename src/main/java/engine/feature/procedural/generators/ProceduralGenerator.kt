package engine.feature.procedural.generators

import engine.core.geometry.Point2D
import engine.feature.procedural.OpenSimplex2S
import engine.feature.procedural.data.ProceduralDataSet
import engine.feature.tiled.data.TileMap

/**
 * (number in tileIdsData) to (number in tileSet)
 */
typealias NormalizedData = MutableList<Pair<Point2D, Int>>
typealias ProceduralData = MutableList<Pair<Point2D, Float>>

class ProceduralGenerator(
    private val dataSet: ProceduralDataSet,
    private val noise: (Long, Double, Double) -> Float = { l, x, y ->
        OpenSimplex2S.noise2(l, x, y)
    },
    private val widthInTiles: Int,
    private val heightInTiles: Int,
    tileSize: Float
) {

    private val worldData: MutableList<Point2D> = mutableListOf()

    private val walkableTerrainGenerator = WalkableTerrainGenerator(
        noiseTypeValues = dataSet.terrainData.noiseParameterTypes,
        targetTypeValues = dataSet.terrainData.elementTypes
    ).apply {
        noiseFunc = noise
    }

    init {
        for (row in 0 until heightInTiles) {
            for (column in 0 until widthInTiles) {
                worldData.add(
                    Point2D(row * tileSize, column * tileSize)
                )
            }
        }

        if (widthInTiles * heightInTiles != worldData.size) {
            throw IllegalStateException("Tiles count is not valid")
        }
    }

    fun generateMap(seed: Long): TileMap {
        val walkableTerrainLayers = walkableTerrainGenerator.generateLayers(
            seed,
            worldData,
            dataSet.terrainData.value,
            widthInTiles,
            heightInTiles
        )
        return TileMap(
            layers = walkableTerrainLayers,
            widthInTiles = widthInTiles,
            heightInTiles = heightInTiles
        )
    }
}