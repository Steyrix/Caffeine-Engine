package engine.feature.tiled.scene

import engine.core.scene.GameContext
import engine.core.scene.Scene
import engine.core.scene.SceneIntent
import engine.core.session.Session
import org.joml.Matrix4f

abstract class TileMapScene(
    projection: Matrix4f
) : Scene {

    override val renderProjection = projection

    override val context: GameContext = GameContext.getInstance()

    protected var tiledMap: TileMapEntity? = null

    override fun init(session: Session, intent: SceneIntent?) {
        super.init(session, intent)

        tiledMap = initTileMap(
            renderProjection,
            screenWidth,
            screenHeight
        )

        tiledMap?.let {
            context.add(it)
            val objects = it.retrieveObjectEntities()
            context.addAll(objects)
        }
    }

    abstract fun initTileMap(
        projection: Matrix4f,
        screenWidth: Float,
        screenHeight: Float
    ): TileMapEntity
}