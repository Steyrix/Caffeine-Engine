package engine.feature.tiled.scene

import engine.core.loop.AccumulatedTimeEvent

data class TileMapPreset(
        val width: Float,
        val height: Float,
        val mapSourcePath: String,
        val vertexShaderPath: String,
        val fragmentShaderPath: String,
        val shaderUniforms: Map<String, Any>,
        val updateEvents: List<AccumulatedTimeEvent>,
        val walkingLayers: List<String>,
        val obstacleLayers: List<String>
)
