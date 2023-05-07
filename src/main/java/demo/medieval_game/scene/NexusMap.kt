package demo.medieval_game.scene

import demo.medieval_game.data.MapSceneInitializer
import demo.medieval_game.data.starting_level.getNexusMapPreset
import engine.core.scene.GameObject
import engine.core.session.Session
import engine.core.window.Window
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.tiled.scene.TileMapObject
import engine.feature.tiled.scene.TileMapScene
import org.joml.Matrix4f

class NexusMap(
        override val screenWidth: Float,
        override val screenHeight: Float,
        projection: Matrix4f? = null,
        switchTrigger: () -> Unit = {}
) : TileMapScene(projection) {

    override val gameContext: MutableList<GameObject> = mutableListOf()

    private val tiledCollisionContext = TiledCollisionContext()

    override fun init(session: Session) {
        if (session !is MedievalGameSession) return

        super.init(session)
    }

    override fun initTileMap(projection: Matrix4f, screenWidth: Float, screenHeight: Float): TileMapObject {
        return MapSceneInitializer.initTileMapObject(
                getNexusMapPreset(screenWidth, screenHeight),
                projection,
                listOf(tiledCollisionContext)
        )
    }

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
}