package engine.feature.tiled.traversing

import java.util.PriorityQueue

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

        val finalDestination = if (graph.getCost(destination) > TileGraph.DEFAULT_COST) {
            graph.getCheapestAdjacent(destination)
        } else {
            destination
        }

        val out = ArrayDeque<Int>()
        val dist = IntArray(graph.nodes.size) { Int.MAX_VALUE }
        val pred = IntArray(graph.nodes.size) { -1 }

        if (!dijkstra(graph, start, finalDestination, dist, pred)) return ArrayDeque()

        out.add(finalDestination)
        graph.increaseCost(finalDestination)
        var previous = finalDestination
        while (pred[previous] != -1) {
            out.addFirst(pred[previous])
            graph.increaseCost(pred[previous])
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
        val queue = PriorityQueue<Int>(compareBy { dist[it] })
        val visited = BooleanArray(graph.nodes.size)

        dist[start] = 0
        queue.add(start)

        while (queue.isNotEmpty()) {
            val node = queue.poll()
            if (visited[node]) continue
            visited[node] = true

            graph.nodes[node]?.forEach {
                val currCost = dist[node] + (graph.costs[it] ?: 0)
                if (currCost < dist[it]) {
                    dist[it] = currCost
                    pred[it] = node
                    queue.add(it)
                }
            }

            if (node == destination) return true
        }

        return false
    }
}