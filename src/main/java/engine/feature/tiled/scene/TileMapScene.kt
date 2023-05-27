package engine.feature.tiled.scene

import engine.core.game_object.GameObject
import engine.core.scene.Scene
import engine.core.scene.SceneIntent
import engine.core.session.Session
import org.joml.Matrix4f

abstract class TileMapScene(
        projection: Matrix4f? = null
) : Scene {

    override var renderProjection: Matrix4f? = null

    override val gameContext: MutableList<GameObject> = mutableListOf()

    protected var tiledMap: TileMapObject? = null

    init {
        projection?.let {
            renderProjection = it
        }
    }

    override fun init(session: Session, intent: SceneIntent?) {
        super.init(session, intent)

        if (renderProjection == null) {
            renderProjection = Matrix4f().ortho(
                    0f,
                    screenWidth,
                    screenHeight,
                    0f,
                    0f,
                    1f
            )
        }

        tiledMap = initTileMap(
                renderProjection!!,
                screenWidth,
                screenHeight
        )

        tiledMap?.let {
            gameContext.add(it)
        }
    }

    abstract fun initTileMap(
            projection: Matrix4f,
            screenWidth: Float,
            screenHeight: Float
    ): TileMapObject
}