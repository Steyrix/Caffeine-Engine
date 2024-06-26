package engine.feature.procedural

import engine.core.geometry.Point2D
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.data.layer.TileLayer
import kotlin.random.Random

// TODO: use noise to generate biomes characteristics rather than tiles itself
object Procedural {

    fun generateLayer(
        set: TileSet,
        widthInTiles: Int = 64,
        heightInTiles: Int = 64,
        tilesCount: Int
    ): TileLayer {
        val rawList = mutableListOf<Float>()
        for (i in 0 until tilesCount) {
            rawList.add(
                getNoiseForCoordinate(
                    determinePosition(i, widthInTiles, set)
                )
            )
        }

        val normalizedList = normalizeForTileset(
            set.getUniqueTilesCount(),
            rawList
        ).toMutableList()

        return TileLayer(
            "",
            widthInTiles,
            heightInTiles,
            set,
            normalizedList,
            mutableListOf()
        )
    }

    private fun generateBiome(): Biome {
        TODO()
    }

    private fun generateBiomeData() {
        TODO()
    }

    private fun determinePosition(
        index: Int,
        widthInTiles: Int,
        set: TileSet
    ): Point2D {
        val rowIndex = index / widthInTiles
        val columnIndex = index - rowIndex * widthInTiles

        val x = columnIndex * set.relativeTileWidth
        val y = rowIndex * set.relativeTileHeight

        return Point2D(x, y)
    }

    private fun normalizeForTileset(
        uniqueTilesCount: Int,
        values: List<Float>
    ): List<Int> {

        val randomSet = mutableSetOf<Int>()

        while (randomSet.size < uniqueTilesCount) {
            randomSet.add(Random.nextInt(uniqueTilesCount))
        }

        val valueMap = hashMapOf<Float, Int>()

        randomSet.forEachIndexed { index, it ->
            if (!valueMap.contains(values[index])) {
                valueMap[values[index]] = it
            }
        }

        return valueMap.values.toList()
    }

    private fun getNoiseForCoordinate(pos: Point2D): Float {
        println(pos)
        return OpenSimplex2S.noise2_ImproveX(
            System.currentTimeMillis(), pos.x.toDouble(), pos.y.toDouble()
        )
    }
}