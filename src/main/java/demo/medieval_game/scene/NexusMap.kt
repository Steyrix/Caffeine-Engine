package demo.medieval_game.scene

import engine.core.scene.GameObject
import engine.core.window.Window
import engine.feature.tiled.scene.TileMapObject
import engine.feature.tiled.scene.TileMapScene
import org.joml.Matrix4f

class NexusMap(
        override val screenWidth: Float,
        override val screenHeight: Float,
        projection: Matrix4f? = null,
        switchTrigger: () -> Unit = {}
) : TileMapScene(projection) {

    override fun initTileMap(projection: Matrix4f, screenWidth: Float, screenHeight: Float): TileMapObject {
        TODO("Not yet implemented")
    }

    override val gameContext: MutableList<GameObject>
        get() = TODO("Not yet implemented")

    override fun input(window: Window) {
        TODO("Not yet implemented")
    }

    override fun update(deltaTime: Float) {
        TODO("Not yet implemented")
    }

    override fun render(window: Window) {
        TODO("Not yet implemented")
    }

    override fun onSwitch() {
        TODO("Not yet implemented")
    }

    override var renderProjection: Matrix4f?
        get() = TODO("Not yet implemented")
        set(value) {}

}