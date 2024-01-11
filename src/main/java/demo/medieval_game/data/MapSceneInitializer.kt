package demo.medieval_game.data

import engine.feature.collision.CollisionContext
import engine.feature.tiled.scene.TileMapEntity
import engine.feature.tiled.scene.TileMapPreset
import org.joml.Matrix4f

object MapSceneInitializer {

    fun initTileMapObject(
        preset: TileMapPreset,
        renderProjection: Matrix4f,
        collisionContexts: List<CollisionContext>,
        isAutoAdjustEnabled: Boolean = false
    ): TileMapEntity {
        return TileMapEntity(preset).apply {
            init(
                renderProjection,
                collisionContexts
            )

            if (isAutoAdjustEnabled) {
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
}