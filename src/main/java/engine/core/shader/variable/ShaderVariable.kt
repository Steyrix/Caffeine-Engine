package engine.core.shader.variable

data class ShaderVariable<T>(
    var value: T,
    val name: String
)