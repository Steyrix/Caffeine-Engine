package engine.feature.procedural.generators

import engine.core.geometry.Point2D

typealias ProceduralData = MutableList<Pair<Point2D, Float>>

abstract class TerrainGenerator(
    private val noiseTypeValues: List<NoiseParameterType> = listOf(),
    private val targetTypeValues: List<ProceduralEnum> = listOf()
) {

    abstract val noiseFunc: (Long, Double, Double) -> Float

    /*
        Algorithm: distribute biomes among tiles' coordinates.
        Then generate noise for each tile.
        Normalize noise afterwards and use it to determine
        which biome-specific tile to set.
    */
    fun generate(
        seed: Long,
        worldData: List<Point2D>,
    ): Map<ProceduralEnum, ProceduralData> {
        val result = hashMapOf<ProceduralEnum, ProceduralData>()

        val proceduralData = distributeProceduralTypes(seed, worldData)

        proceduralData.keys.forEach { point ->
            val noiseValue = getNoiseForCoordinate(seed, point)

            proceduralData[point]?.let { proceduralType ->
                if (!result.contains(proceduralType)) {
                    result[proceduralType] = mutableListOf()
                }
                result[proceduralType]?.add(point to noiseValue)
            }
        }

        return result
    }


    private fun getNoiseForCoordinate(
        seed: Long,
        pos: Point2D
    ): Float {
        return noiseFunc(
            seed,
            pos.x.toDouble(),
            pos.y.toDouble()
        )
    }

    private fun distributeProceduralTypes(
        seed: Long,
        worldData: List<Point2D>
    ): Map<Point2D, ProceduralEnum> {
        val result = mutableMapOf<Point2D, ProceduralEnum>()

        worldData.forEach {

            val noiseParameters = mutableListOf<NoiseParameter>()

            noiseTypeValues.forEach { type ->
                noiseParameters.add(
                    NoiseParameter(getNoiseForCoordinate(seed, it), type)
                )
            }

            val proceduralValueType = targetTypeValues.last { type ->
                type.checkIfMatch(noiseParameters)
            }

            result[it] = proceduralValueType
        }

        return result
    }
}