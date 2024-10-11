package engine.feature.procedural.autotiling

data class AutoTile(
    val id: Int,
    val northId: Int,
    val westId: Int,
    val southId: Int,
    val eastId: Int
)