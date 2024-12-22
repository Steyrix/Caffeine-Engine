package engine.feature.procedural.generators

import kotlin.math.sqrt
import kotlin.random.Random

object VoronoiBasedGenerator {

    fun generate(
        mapWidth: Int,
        mapHeight: Int,
        numSeeds: Int,
        biomeStringKeys: List<String> = listOf("Grass", "Stone", "Dirt"),
        seed: Int
    ): Array<Array<String>> {
        val points = generateSeedPoints(mapWidth, mapHeight, numSeeds, seed)
        val voronoiMap = calculateVoronoiMap(mapWidth, mapHeight, points)
        return assignBiomes(voronoiMap, biomeStringKeys)
    }

    private fun generateSeedPoints(
        width: Int,
        height: Int,
        numSeeds: Int,
        seed: Int
    ): List<Pair<Float, Float>> {
        val xRand = Random(seed)
        val yRand = Random(seed)

        return List(numSeeds) {
            Pair(
                xRand.nextInt(0, width).toFloat(),
                yRand.nextInt(0, height).toFloat()
            )
        }
    }

    private fun calculateVoronoiMap(
        width: Int,
        height: Int,
        seedPoints: List<Pair<Float, Float>>
    ): Array<IntArray> {
        val voronoiMap = Array(height) { IntArray(width) { -1 } }

        for (y in 0 until height) {
            for (x in 0 until width) {
                var minDist = Double.MAX_VALUE
                var closestSeed = -1
                for ((i, seed) in seedPoints.withIndex()) {
                    val (sx, sy) = seed
                    val dist = sqrt(((x - sx) * (x - sx) + (y - sy) * (y - sy)).toDouble())
                    if (dist < minDist) {
                        minDist = dist
                        closestSeed = i
                    }
                }
                voronoiMap[y][x] = closestSeed
            }
        }

        return voronoiMap
    }

    private fun assignBiomes(
        voronoiMap: Array<IntArray>,
        biomeStringKeys: List<String>
    ): Array<Array<String>> {
        val biomeMap = Array(voronoiMap.size) { Array(voronoiMap[0].size) { "" } }
        for (y in voronoiMap.indices) {
            for (x in voronoiMap[y].indices) {
                val cell = voronoiMap[y][x]
                biomeMap[y][x] = biomeStringKeys[cell % biomeStringKeys.size]
            }
        }

        return biomeMap
    }


}