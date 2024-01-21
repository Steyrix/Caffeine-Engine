package engine.feature.animation

import engine.core.shader.Shader

interface Animation {
    val name: String

    val isCycled: Boolean
        get() = true

    fun play(deltaTime: Float)

    fun setUniforms(shader: Shader)

    fun copy(): Animation
}