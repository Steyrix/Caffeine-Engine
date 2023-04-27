package demo.labyrinth.data

import demo.labyrinth.data.gameobject.*
import engine.core.scene.GameObject
import engine.core.scene.SceneInitializer
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.getCenterPoint
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.matrix.MatrixState
import engine.feature.tiled.scene.TileMapObject
import engine.feature.tiled.traversing.TileTraverser
import org.joml.Matrix4f

object LabyrinthInitializer : SceneInitializer {

    fun initAll(
            renderProjection: Matrix4f,
            screenWidth: Float,
            screenHeight: Float,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            tiledCollisionContext: TiledCollisionContext,
            boxInteractionContext: BoxInteractionContext
    ): MutableList<GameObject> {
        val tempSpritesHolder = TempSpritesHolder().apply {
            init(renderProjection)
        }

        val newMap = TileMapObject(
                getStartingMapPreset(screenWidth, screenHeight)
        ).apply {
            init(
                    renderProjection,
                    listOf(tiledCollisionContext)
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

        val character = Character().apply {
            init(
                    renderProjection,
                    boundingBoxCollisionContext,
                    tiledCollisionContext,
                    boxInteractionContext,
                    tempSpritesHolder
            )
        }

        val campfire = Campfire().apply {
            init(renderProjection)
        }

        val listOfNpc = initGoblins(
                renderProjection,
                boundingBoxCollisionContext,
                boxInteractionContext,
                tempSpritesHolder
        ) { params: SetOf2DParametersWithVelocity -> newMap.createTraverser(params) }

        val centerPoint = characterParameters.getCenterPoint()
        MatrixState.translate(
                screenWidth / 2 - centerPoint.x,
                screenHeight / 2 - centerPoint.y
        )

        return mutableListOf(newMap, campfire, character, tempSpritesHolder).also { it.addAll(listOfNpc) }
    }

    private fun initGoblins(
            renderProjection: Matrix4f,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            boxInteractionContext: BoxInteractionContext,
            tempSpritesHolder: TempSpritesHolder,
            creator: (SetOf2DParametersWithVelocity) -> TileTraverser
    ): List<NpcEnemy> {
        val out = mutableListOf<NpcEnemy>()

        val enemy1 = NpcEnemy(goblinParams1)
                .also { npc ->
                    npc.init(
                            renderProjection,
                            boundingBoxCollisionContext,
                            boxInteractionContext,
                            creator(goblinParams1),
                            tempSpritesHolder
                    )
                }
        val enemy2 = NpcEnemy(goblinParams2)
                .also { npc ->
                    npc.init(
                            renderProjection,
                            boundingBoxCollisionContext,
                            boxInteractionContext,
                            creator(goblinParams2),
                            tempSpritesHolder
                    )
                }

        out.add(enemy1)
        out.add(enemy2)

        return out
    }
}