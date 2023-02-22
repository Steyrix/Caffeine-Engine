package engine.feature.tiled.traversing

object ShortestPath {

    fun pathTo(
            graph: Map<Int, List<Int>>,
            start: Int,
            destination: Int
    ): ArrayDeque<Int> {
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
}