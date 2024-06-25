package engine.feature.procedural

enum class BiomeType {
    FOREST,
    PLAINS,
    DESERT
}

data class Biome(
    val type: BiomeType
)
