package engine.feature.tiled.traversing

class TileGraph(
        val nodes: Map<Int, List<Int>>
) {
    companion object {
        const val DEFAULT_DISTANCE = 1
    }

    val distinct: MutableSet<Int> = mutableSetOf()

    val distances: MutableMap<Int, Int> = mutableMapOf()

    init {
        nodes.forEach {
            distances[it.key] = DEFAULT_DISTANCE
            distinct.add(it.key)
        }
    }

    fun increaseDistance(target: Int) {
        distances[target] = distances.getOrDefault(target, 0) + 1
    }

    fun decreaseDistance(target: Int) {
        distances[target] = if (distances[target] == 0) {
            0
        } else {
            distances.getOrDefault(target, 0) - 1
        }
    }

    fun getClosestAdjacent(target: Int): Int {
        var min = Int.MAX_VALUE
        var out = target
        nodes[target]?.forEach {
            if (distances.getOrDefault(it, min) < min) {
                out = it
                min = distances.getOrDefault(it, min)
            }
        }

        return out
    }
}