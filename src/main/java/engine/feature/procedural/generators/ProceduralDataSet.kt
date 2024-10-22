package engine.feature.procedural.generators

import engine.feature.procedural.MapElementType
import engine.feature.tiled.data.TileSet

data class ProceduralDataSet(
    val terrainData: Map<MapElementType, TileSet>
)
