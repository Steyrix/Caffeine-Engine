package engine.feature.tiled.traversing

class TileGraph(
    val nodes: Map<Int, List<Int>>
) {
    companion object {
        const val DEFAULT_COST = 1
    }

    val distinct: MutableSet<Int> = mutableSetOf()

    val costs: MutableMap<Int, Int> = mutableMapOf()

    init {
        nodes.forEach {
            costs[it.key] = DEFAULT_COST
            distinct.add(it.key)
        }
    }

    fun increaseCost(target: Int) {
        costs[target] = costs.getOrDefault(target, DEFAULT_COST) + 1
    }

    fun decreaseCost(target: Int) {
        costs[target] = if (costs[target] == 0) {
            0
        } else {
            costs.getOrDefault(target, 0) - 1
        }
    }

    fun getCost(target: Int): Int {
        return costs[target] ?: DEFAULT_COST
    }

    fun getCheapestAdjacent(target: Int): Int {
        var min = Int.MAX_VALUE
        var out = nodes[target]?.first() ?: target
        nodes[target]?.forEach {
            if (costs.getOrDefault(it, min) < min) {
                out = it
                min = costs.getOrDefault(it, min)
            }
        }

        return out
    }
}