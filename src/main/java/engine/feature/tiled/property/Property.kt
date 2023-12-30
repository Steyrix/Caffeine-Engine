package engine.feature.tiled.property

sealed class Property(
    val propertyName: String
)

data class IntProperty(
    val value: Int,
    val name: String
) : Property(name)

data class FloatProperty(
    val value: Float,
    val name: String
) : Property(name)

data class BooleanProperty(
    val value: Boolean,
    val name: String
) : Property(name)

data class StringProperty(
    val value: String,
    val name: String
) : Property(name)
