package engine.feature.procedural.generators

import engine.core.geometry.Point2D
import engine.feature.procedural.NoiseParameter
import engine.feature.procedural.NoiseParameterType
import engine.feature.procedural.MapElementType

class TerrainGenerator(
    private val noiseTypeValues: List<NoiseParameterType> = listOf(),
    private val targetTypeValues: List<MapElementType> = listOf()
) : AbstractGenerator() {

   override var noiseFunc: (Long, Double, Double) -> Float = { _, _, _ ->
       0f
   }

    /*
        Algorithm: distribute biomes among tiles' coordinates.
        Then generate noise for each tile.
        Normalize noise afterwards and use it to determine
        which biome-specific tile to set.
    */
    fun generate(
        seed: Long,
        worldData: List<Point2D>,
    ): Map<MapElementType, ProceduralData> {
        val result = hashMapOf<MapElementType, ProceduralData>()

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

    private fun distributeProceduralTypes(
        seed: Long,
        worldData: List<Point2D>
    ): Map<Point2D, MapElementType> {
        val result = mutableMapOf<Point2D, MapElementType>()

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