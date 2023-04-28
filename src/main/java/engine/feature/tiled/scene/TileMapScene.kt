package engine.feature.tiled.scene

import engine.core.scene.Scene
import engine.core.session.Session
import org.joml.Matrix4f

abstract class TileMapScene(
        projection: Matrix4f? = null
) : Scene {

    init {
        projection?.let {
            renderProjection = it
        }
    }

    protected var tiledMap: TileMapObject? = null

    override fun init(session: Session) {
        super.init(session)

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