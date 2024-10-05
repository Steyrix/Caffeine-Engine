package engine.feature.procedural.generators

import engine.core.geometry.Point2D
import engine.feature.procedural.MapElementType
import engine.feature.procedural.NoiseParameterType
import engine.feature.procedural.OpenSimplex2S
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.data.layer.Layer
import engine.feature.tiled.data.layer.TileLayer

/**
 * (number in tileIdsData) to (number in tileSet)
 */
typealias NormalizedData = MutableList<Pair<Point2D, Int>>

const val WALKABLE_MARK = "walkable_layer"

class ProceduralGenerator(
    private val tileSets: Map<MapElementType, TileSet>,
    private val noise: (Long, Double, Double) -> Float = { l, x, y ->
        OpenSimplex2S.noise2(l, x, y)
    },
    private val widthInTiles: Int,
    private val heightInTiles: Int,
    tileSize: Float,
    elementTypes: List<MapElementType> = emptyList(),
    noiseParametersTypes: List<NoiseParameterType> = emptyList()
) {

    private val worldData: MutableList<Point2D> = mutableListOf()

    private val terrainGenerator = object : TerrainGenerator(
        noiseTypeValues = noiseParametersTypes,
        targetTypeValues = elementTypes
    ) {
        override val noiseFunc: (Long, Double, Double) -> Float = noise
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
        val walkableLayers = generateWalkableLayers(seed)
        return TileMap(
            layers = walkableLayers,
            widthInTiles = widthInTiles,
            heightInTiles = heightInTiles
        )
    }

    private fun generateWalkableLayers(
        seed: Long
    ): List<Layer> {
        val data = terrainGenerator.generate(
            seed, worldData
        )

        val resultMap = hashMapOf<TileSet, NormalizedData>()

        /**
         * Each element type (e.g. biome) refers to a collection of noise values and a tileset.
         * Former is being normalized for latter.
         */
        data.keys.forEach {
            val targetSet = tileSets[it]
            data[it]?.let { proceduralData ->
                val normalizedValues = normalizeForTileSet(proceduralData, targetSet!!)
                resultMap[targetSet] = normalizedValues
            }
        }

        val layers: MutableList<Layer> = resultMap.keys.mapIndexed { index, it ->
            val tileIds = resultMap[it]!!
                .map {
                    it.first.toTileId() to it.second
                }.sortedBy { it.first }
                .map { it.second }
                .toMutableList()

            TileLayer(
                name = "walkable_layer",
                widthInTiles = widthInTiles,
                heightInTiles = heightInTiles,
                set = it,
                tileIdsData = tileIds, // TODO
                properties = mutableListOf(),
                model = null
            )
        }.toMutableList()


        return layers
    }


    /**
     * This method maps noise values to tileset tiles' ids.
     * Firstly it creates a list of available ids. After that each noise value is mapped to a random id.
     *
     * @param values is a list of points mapped to noise values, where each point is unique and refers to the start
     * (top-left corner position) of the tile.
     * @param set is a target tileset, which will be used as a source for rendering a layer.
     */
    private fun normalizeForTileSet(values: List<Pair<Point2D, Float>>, set: TileSet): NormalizedData {
        val count = set.getUniqueTilesCount()
        val setOfIds = (0 until count).toList()

        val valueMap = hashMapOf<Float, Pair<Point2D, Int>>()
        val result: NormalizedData = mutableListOf()

        values.forEach {
            val noisedValue = it.second
            if (!valueMap.contains(noisedValue)) {
                valueMap[noisedValue] = it.first to setOfIds.random()
            }
        }

        for (i in values.indices) {
            result.add(
                valueMap[values[i].second]!!
            )
        }

        return result
    }

    private fun Point2D.toTileId(): Int {
        worldData.forEachIndexed { index, it ->
            if (it == this) {
                return index
            }
        }

        return 0
    }
}