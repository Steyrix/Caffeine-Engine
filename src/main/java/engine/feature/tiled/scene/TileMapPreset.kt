package engine.feature.tiled.scene

import engine.core.loop.AccumulatedTimeEvent
import engine.core.render.Drawable

data class TileMapPreset(
    val width: Float,
    val height: Float,
    val mapSourcePath: String,
    val vertexShaderPath: String,
    val fragmentShaderPath: String,
    val objectVertexShaderPath: String,
    val objectFragmentShaderPath: String,
    val shaderUniforms: Map<String, Any>,
    val updateEvents: List<(Drawable<*>) -> AccumulatedTimeEvent>,
    val walkingLayers: List<String>,
    val obstacleLayers: List<String>
)

data class ProceduralMapPreset(
    val width: Float,
    val height: Float,
    val vertexShaderPath: String,
    val fragmentShaderPath: String,
    val objectVertexShaderPath: String,
    val objectFragmentShaderPath: String,
    val shaderUniforms: Map<String, Any>,
    val updateEvents: List<(Drawable<*>) -> AccumulatedTimeEvent>,
)
