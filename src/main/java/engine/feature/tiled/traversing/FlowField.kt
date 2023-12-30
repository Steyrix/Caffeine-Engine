package engine.feature.tiled.traversing

import engine.feature.tiled.data.TileMap

class FlowField(
    map: TileMap
) {
    private val occupationMap: MutableMap<Int, Int>

    init {
        var counter = 0
        occupationMap = mutableMapOf()

        while (counter < map.tilesCount) {
            occupationMap[counter++] = 0
        }
    }

    fun increaseCost(tileIndex: Int) {
        occupationMap[tileIndex] = increment(tileIndex)
    }

    fun decreaseCost(tileIndex: Int) {
        occupationMap[tileIndex] = decrement(tileIndex)
    }

    fun getCost(tileIndex: Int) = occupationMap[tileIndex] ?: 0

    private fun increment(index: Int): Int {
        return occupationMap[index]?.let {
            it + 1
        } ?: 1
    }

    private fun decrement(index: Int): Int {
        return occupationMap[index]?.let {
            if (it == 0) 0
            else it - 1
        } ?: 0
    }
}