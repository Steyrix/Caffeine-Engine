package engine.feature.tiled.data

import engine.core.shader.Shader

data class TileMapShaders(
    var objectShader: Shader? = null,
    var objectShaderCreator: (() -> Shader?)? = null,
    var debugShader: Shader? = null
)
