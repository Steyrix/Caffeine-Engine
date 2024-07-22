package engine.feature.tiled.data

import engine.core.shader.Shader

data class TileMapShaders(
    val mainShader: Shader?,
    val objectShader: Shader?,
    val objectShaderCreator: () -> Shader?,
    val debugShader: Shader?
)
