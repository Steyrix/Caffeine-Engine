package engine.feature.animation

import engine.core.shader.Shader

interface Animation {
    val name: String

    val isReplayable: Boolean
        get() = true

    fun play(deltaTime: Float)

    fun setUniforms(shader: Shader)
}