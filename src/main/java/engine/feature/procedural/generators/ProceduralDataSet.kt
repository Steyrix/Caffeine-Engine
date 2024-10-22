package engine.feature.procedural.generators

import engine.feature.procedural.MapElementType
import engine.feature.tiled.data.TileSet

data class ProceduralDataSet(
    val terrainData: Map<MapElementType, TileSet>,
    val unwalkableTerrainData: Map<MapElementType, TileSet>,
    val backgroundStructuresData: List<TileSet>,
    val objectStructuresData: List<TileSet>
)
