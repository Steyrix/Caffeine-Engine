package engine.feature.procedural.generators

import engine.core.geometry.Point2D
import engine.feature.procedural.MapElementType
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.data.layer.TileLayer
import kotlin.random.Random

// TODO: (number in tileIdsData) to (number in tileSet)
typealias NormalizedData = MutableList<Pair<Point2D, Int>>

class ProceduralGenerator(
    private val tileSets: Map<MapElementType, TileSet>,
    private val noise: (Long, Double, Double) -> Float,
    private val widthInTiles: Int,
    private val heightInTiles: Int,
    tileSize: Float
) {

    private val worldData: MutableList<Point2D> = mutableListOf()

    private val terrainGenerator = object : TerrainGenerator() {
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

    fun generateMap(
        seed: Long
    ): TileMap {
        val data = terrainGenerator.generate(
            seed, worldData
        )

        val resultMap = hashMapOf<TileSet, NormalizedData>()

        // TODO: move to separate method
        data.keys.forEach {
            val targetSet = tileSets[it]
            data[it]?.let { proceduralData ->
                val normalizedValues = normalizeForTileSet(proceduralData, targetSet!!)
                resultMap[targetSet] = normalizedValues
            }
        }


        val layers = resultMap.keys.mapIndexed { index, it ->
            val tileIds = resultMap[it]!!
                .map {
                    it.first.toTileId() to it.second
                }.sortedBy { it.first }
                .map { it.second }
                .toMutableList()

            TileLayer(
                name = "ProceduralLayer_$index",
                widthInTiles = widthInTiles,
                heightInTiles = heightInTiles,
                set = it,
                tileIdsData = tileIds, // TODO
                properties = mutableListOf(),
                model = null
            )
        }

        TODO()
    }


    private fun normalizeForTileSet(values: List<Pair<Point2D, Float>>, set: TileSet): NormalizedData {
        val count = set.getUniqueTilesCount()
        val randomSet = mutableSetOf<Int>()

        while (randomSet.size < count) {
            randomSet.add(Random.nextInt(count))
        }

        val valueMap = hashMapOf<Float, Pair<Point2D, Int>>()

        randomSet.forEachIndexed { index, it ->
            val noisedValue = values[index].second
            if (!valueMap.contains(noisedValue)) {
                valueMap[noisedValue] = values[index].first to it
            }
        }

        return valueMap.values.toList().toMutableList()
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