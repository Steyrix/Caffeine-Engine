package engine.feature.procedural.generators

import engine.core.geometry.Point2D
import engine.feature.procedural.MapElementType
import engine.feature.tiled.data.TileSet
import kotlin.random.Random

typealias NormalizedData = MutableList<Pair<Point2D, Int>>

class ProceduralGenerator(
    private val tileSets: Map<MapElementType, TileSet>,
    private val noise: (Long, Double, Double) -> Float,
    private val worldData: List<Point2D>
) {
    private val terrainGenerator = object : TerrainGenerator() {
        override val noiseFunc: (Long, Double, Double) -> Float = noise
    }

    fun generateMap(
        seed: Long
    ) {
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

        TODO("generate tile map")
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
}