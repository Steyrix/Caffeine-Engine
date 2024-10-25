package engine.feature.procedural.generators

import engine.feature.procedural.MapElementType
import engine.feature.procedural.NoiseParameterType
import engine.feature.tiled.data.TileSet

data class TerrainData(
    val value: Map<MapElementType, TileSet>,
    val elementTypes: List<MapElementType>,
    val noiseParameterTypes: List<NoiseParameterType>
)

data class ProceduralDataSet(
    val terrainData: TerrainData,
    val unwalkableTerrainData: Map<MapElementType, TileSet>,
    val backgroundStructuresData: List<TileSet>,
    val objectStructuresData: List<TileSet>
)
