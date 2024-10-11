package engine.feature.procedural.autotiling

data class AdjacentIds(
    val northId: Int,
    val westId: Int,
    val southId: Int,
    val eastId: Int
)

data class AdjacentPlacesInMap(
    val northPlaceInMap: Int,
    val westPlaceInMap: Int,
    val southPlaceInMap: Int,
    val eastPlaceInMap: Int
)

data class AutoTile(
    val id: Int,
    val adjacentIds: AdjacentIds
)