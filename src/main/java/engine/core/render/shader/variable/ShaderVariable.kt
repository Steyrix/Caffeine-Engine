package engine.core.render.shader.variable

data class ShaderVariable<T>(
    var value: T,
    val name: String
)