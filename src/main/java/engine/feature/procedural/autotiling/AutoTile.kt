package engine.feature.procedural.autotiling

data class Adjacent(
    val northId: Int,
    val westId: Int,
    val southId: Int,
    val eastId: Int
)

data class AutoTile(
    val id: Int,
    val adjacent: Adjacent
)