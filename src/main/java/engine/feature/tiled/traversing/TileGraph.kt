package engine.feature.tiled.traversing

class TileGraph(
        val nodes: Map<Int, List<Int>>
) {
    val distinct: MutableSet<Int> = mutableSetOf()

    val distances: MutableMap<Int, Int> = mutableMapOf()

    init {
        nodes.forEach {
            distances[it.key] = 1
            distinct.add(it.key)
        }
    }

    fun increaseDistance(target: Int) {
        distances[target] = distances.getOrDefault(target, 0) + 1
    }

    fun decreaseDistance(target: Int) {
        distances[target]?.inc()
    }
}