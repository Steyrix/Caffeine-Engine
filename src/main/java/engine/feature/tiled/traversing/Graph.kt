package engine.feature.tiled.traversing

class Node<T>(
        val adjacencyNodes: MutableSet<Node<T>>,
        val value: T
)

class Graph<T>(
        val nodes: MutableSet<Node<T>>
)