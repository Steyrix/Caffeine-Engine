package engine.feature.animation

import engine.core.shader.Shader

interface Animation {
    val name: String

    fun play(deltaTime: Float)

    fun setUniforms(shader: Shader)
}