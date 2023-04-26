package engine.feature.tiled.scene

import engine.core.scene.Scene
import org.joml.Matrix4f

// TODO: finish, test
abstract class TileMapScene : Scene {

    private var tiledMap: TileMapObject? = null

    override fun init() {
        renderProjection = Matrix4f().ortho(
                0f,
                screenWidth,
                screenHeight,
                0f,
                0f,
                1f
        )

        tiledMap = initTileMap(
                renderProjection!!,
                screenWidth,
                screenHeight
        )
    }

    abstract fun initTileMap(
            projection: Matrix4f,
            screenWidth: Float,
            screenHeight: Float
    ): TileMapObject
}