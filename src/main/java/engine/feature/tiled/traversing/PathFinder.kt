package engine.feature.tiled.traversing

object PathFinder {

    fun pathTo(
            graph: Map<Int, List<Int>>,
            start: Int,
            destination: Int
    ): ArrayDeque<Int> {
        if (graph[destination].isNullOrEmpty() || graph[start].isNullOrEmpty()) return ArrayDeque()

        val out = ArrayDeque<Int>()
        val dist = IntArray(graph.size) { Int.MAX_VALUE }
        val pred = IntArray(graph.size) { -1 }

        if (!bfs(graph, start, destination, dist, pred)) return ArrayDeque()

        out.add(destination)
        var previous = destination
        while (pred[previous] != -1) {
            out.addFirst(pred[previous])
            previous = pred[previous]
        }

        return out
    }

    private fun bfs(
            graph: Map<Int, List<Int>>,
            start: Int,
            destination: Int,
            dist: IntArray,
            pred: IntArray
    ): Boolean {
        val queue = ArrayDeque<Int>()

        val visited = BooleanArray(graph.size) { false }

        visited[start] = true
        dist[start] = 0
        queue.add(start)

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()

            graph[node]!!.forEachIndexed { index, it ->
                if (!visited[it]) {
                    visited[it] = true
                    dist[it] = dist[node] + 1
                    pred[it] = node
                    queue.add(it)

                    if (it == destination) return true
                }
            }
        }

        return false
    }

    fun pathToByDijkstra(
            graph: TileGraph,
            start: Int,
            destination: Int
    ): ArrayDeque<Int> {
        if (graph.nodes[destination].isNullOrEmpty() || graph.nodes[start].isNullOrEmpty()) return ArrayDeque()

        val out = ArrayDeque<Int>()
        val dist = IntArray(graph.nodes.size) { Int.MAX_VALUE }
        val pred = IntArray(graph.nodes.size) { -1 }

        if (!dijkstra(graph, start, destination, dist, pred)) return ArrayDeque()

        out.add(destination)
        var previous = destination
        while (pred[previous] != -1) {
            out.addFirst(pred[previous])
            graph.increaseDistance(pred[previous])
            previous = pred[previous]
        }

        return out
    }

    private fun dijkstra(
            graph: TileGraph,
            start: Int,
            destination: Int,
            dist: IntArray,
            pred: IntArray
    ): Boolean {
        val queue = mutableSetOf(*graph.distinct.toTypedArray())

        dist[start] = 0

        while (queue.isNotEmpty()) {
            val node = queue.minByOrNull { dist[it] } ?: 0
            queue.remove(node)

            if (node == destination) return true

            graph.nodes[node]!!.forEach {
                val currDistance = dist[node] + (graph.distances[it] ?: 0)
                if (currDistance < dist[node]) {
                    dist[node] = currDistance
                    pred[node] = it
                }
            }
        }

        return false
    }
}