package engine.feature.tiled.scene

import engine.core.game_object.GameEntity
import engine.core.scene.Scene
import engine.core.scene.SceneIntent
import engine.core.session.Session
import org.joml.Matrix4f

abstract class TileMapScene(
        projection: Matrix4f
) : Scene {

    override val renderProjection = projection

    override val gameContext: MutableList<GameEntity> = mutableListOf()

    protected var tiledMap: TileMapEntity? = null

    override fun init(session: Session, intent: SceneIntent?) {
        super.init(session, intent)

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
    ): TileMapEntity
}