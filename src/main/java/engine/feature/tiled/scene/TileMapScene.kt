package engine.feature.tiled.scene

import engine.core.scene.Scene
import org.joml.Matrix4f

// TODO: finish
abstract class TileMapScene : Scene {

    protected var tiledMap: TileMapObject? = null

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

        tiledMap?.let {
            gameContext[it] = it.parameters
        }
    }

    abstract fun initTileMap(
            projection: Matrix4f,
            screenWidth: Float,
            screenHeight: Float
    ): TileMapObject
}