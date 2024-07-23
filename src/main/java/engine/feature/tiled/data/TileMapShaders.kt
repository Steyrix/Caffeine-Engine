package engine.feature.tiled.data

import engine.core.shader.Shader

data class TileMapShaders(
    val mainShader: Shader? = null,
    val objectShader: Shader? = null,
    val objectShaderCreator: (() -> Shader?)? = null,
    val debugShader: Shader? = null
)
