package engine.core.scene

typealias AdjacentScenes<S> = MutableMap<Node<S>, MutableList<Node<S>>>

abstract class Node<S : Scene> {
    protected abstract val value: S
}

abstract class SceneGraph<S : Scene> {

    protected abstract val nodes: AdjacentScenes<S>
}