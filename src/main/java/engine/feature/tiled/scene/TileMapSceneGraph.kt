package engine.feature.tiled.scene

import engine.core.scene.AdjacentScenes
import engine.core.scene.Node
import engine.core.scene.SceneGraph

class TileMapSceneNode : Node<TileMapScene>() {

    override val value: TileMapScene
        get() = TODO("Not yet implemented")
}

class TileMapSceneGraph : SceneGraph<TileMapScene, TileMapSceneNode>() {

    override val nodes: AdjacentScenes<TileMapSceneNode> = mutableMapOf()
}