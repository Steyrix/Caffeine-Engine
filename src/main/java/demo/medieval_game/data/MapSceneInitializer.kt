package demo.medieval_game.data

import engine.feature.collision.CollisionContext
import engine.feature.tiled.scene.TileMapObject
import engine.feature.tiled.scene.TileMapPreset
import org.joml.Matrix4f

object MapSceneInitializer {

    fun initTileMapObject(
            preset: TileMapPreset,
            renderProjection: Matrix4f,
            collisionContexts: List<CollisionContext>
    ): TileMapObject {
        return TileMapObject(preset).apply {
            init(
                    renderProjection,
                    collisionContexts
            )
            adjustParameters(
                    HUMANOID_SIZE_TO_MAP_RELATION,
                    listOf(
                            characterParameters,
                            goblinParams1,
                            goblinParams2
                    )
            )
        }
    }
}