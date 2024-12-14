package engine.feature.tiled.data

import engine.core.render.shader.Shader

data class TileMapShaders(
    var mainShader: Shader? = null,
    var objectShaderCreator: () -> Shader? = { null },
    var debugShader: Shader? = null
)
