package engine.core.scene

typealias AdjacentScenes<N> = MutableMap<N, MutableList<N>>

abstract class Node<S : Scene> {
    protected abstract val value: S
}

abstract class SceneGraph<S : Scene, N : Node<S>> {

    protected abstract val nodes: AdjacentScenes<N>
}