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
        val data = generate(
            seed, worldData
        )

        val resultMap = hashMapOf<TileSet, NormalizedData>()

        /**
         * Each element type (e.g. biome) refers to a collection of noise values and a tileset.
         * Former is being normalized for latter.
         */
        data.keys.forEach {
            val targetSet = terrainData[it]
            data[it]?.let { proceduralData ->
                val normalizedValues = GeneratorUtil.normalizeForTileSet(proceduralData, targetSet!!)
                resultMap[targetSet] = normalizedValues
            }
        }

        val layers: MutableList<Layer> = resultMap.keys.mapIndexed { _, it ->
            val tileIds = resultMap[it]!!
                .map {
                    it.first.toTileId(worldData) to it.second
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

    /*
        Algorithm: distribute biomes among tiles' coordinates.
        Then generate noise for each tile.
        Normalize noise afterwards and use it to determine
        which biome-specific tile to set.
    */
    private fun generate(
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