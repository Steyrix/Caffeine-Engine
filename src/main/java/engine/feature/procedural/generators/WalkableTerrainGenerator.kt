package engine.feature.procedural.generators

import engine.core.geometry.Point2D
import engine.feature.procedural.NoiseParameter
import engine.feature.procedural.NoiseParameterType
import engine.feature.procedural.MapElementType
import engine.feature.procedural.generators.GeneratorUtil.toTileId
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.data.layer.Layer
import engine.feature.tiled.data.layer.TileLayer

class WalkableTerrainGenerator(
    private val noiseTypeValues: List<NoiseParameterType> = listOf(),
    private val targetTypeValues: List<MapElementType> = listOf(),
    override var noiseFunc: (Long, Double, Double) -> Float
) : AbstractGenerator() {

    fun generateLayers(
        seed: Long,
        worldData: List<Point2D>,
        terrainData: Map<MapElementType, TileSet>,
        widthInTiles: Int,
        heightInTiles: Int
    ): List<Layer> {
        val data: Map<MapElementType, PointToNoiseValueList> = generate(
            seed, worldData
        )

        val resultMap = hashMapOf<TileSet, PointToTileIdList>()

        data.keys.forEach {
            val targetSet = terrainData[it]
            data[it]?.let { proceduralData ->
                val normalizedValues = proceduralData.map { pair ->
                    GeneratorUtil.normalizeForTileSet(pair, targetSet!!)
                }.toMutableList()

                targetSet?.let { set ->
                    resultMap[set] = normalizedValues
                }
            }
        }

        val layers: MutableList<Layer> = resultMap.keys.mapIndexed { index, tileSet ->
            val tileIds = resultMap[tileSet]!!
                .map {
                    it.first.toTileId(worldData) to it.second
                }.sortedBy { it.first }
                .map { it.second }
                .toMutableList()

            TileLayer(
                name = "walkable_layer_$index",
                widthInTiles = widthInTiles,
                heightInTiles = heightInTiles,
                set = tileSet,
                tileIdsData = tileIds,
                properties = mutableListOf(),
                model = null
            )
        }.toMutableList()


        return layers
    }

    private fun generate(
        seed: Long,
        worldData: List<Point2D>,
    ): Map<MapElementType, PointToNoiseValueList> {
        val result = hashMapOf<MapElementType, PointToNoiseValueList>()
        targetTypeValues.forEach {
            result[it] = mutableListOf()
        }

        val proceduralData = distributeProceduralTypes(seed, worldData)

        targetTypeValues.forEach { biome ->
            worldData.forEach { point ->
                if (proceduralData[biome]?.contains(point) == true) {
                    result[biome]?.add(point to getNoiseForCoordinateWithFrequency(seed, point))
                } else {
                    result[biome]?.add(point to null)
                }
            }
        }

        return result
    }

    private fun distributeProceduralTypes(
        seed: Long,
        worldData: List<Point2D>
    ): Map<MapElementType, MutableList<Point2D>> {
        val result = mutableMapOf<MapElementType, MutableList<Point2D>>()
        targetTypeValues.forEach {
            result[it] = mutableListOf()
        }

        worldData.forEach { mapPoint ->
            val noiseParameters = mutableListOf<NoiseParameter>()

            noiseTypeValues.forEach { type ->
                noiseParameters.add(
                    NoiseParameter(
                        getNoiseForCoordinateWithFrequency(
                            seed,
                            mapPoint,
                            frequency = 0.1
                        ),
                        type
                    )
                )
            }

            val proceduralValueType = targetTypeValues.lastOrNull { type ->
                type.checkIfMatch(noiseParameters)
            }

            if (proceduralValueType == null) {
                noiseParameters.forEach { param ->
                    targetTypeValues.forEach { type ->
                        if (type.checkIfMatch(listOf(param))) {
                            result[type]?.add(mapPoint)
                        }
                    }
                }
            } else {
                result[proceduralValueType]?.add(mapPoint)
            }
        }

        return result
    }
}