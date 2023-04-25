package demo.labyrinth.data

import demo.labyrinth.data.gameobject.*
import engine.core.scene.GameObject
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.getCenterPoint
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.matrix.MatrixState
import engine.feature.tiled.traversing.TileTraverser
import org.joml.Matrix4f

object LabyrinthInitializer {

    fun initAll(
            renderProjection: Matrix4f,
            screenWidth: Float,
            screenHeight: Float,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            tiledCollisionContext: TiledCollisionContext,
            boxInteractionContext: BoxInteractionContext
    ): MutableList<GameObject> {
        val gameMap = GameMap().apply {
            init(
                    renderProjection,
                    screenWidth,
                    screenHeight,
                    tiledCollisionContext
            )
        }

        val character = Character().apply {
            init(
                    renderProjection,
                    boundingBoxCollisionContext,
                    tiledCollisionContext,
                    boxInteractionContext
            )
        }

        val campfire = Campfire().apply {
            init(renderProjection)
        }

        val listOfNpc = initGoblins(
                renderProjection,
                boundingBoxCollisionContext,
                boxInteractionContext
        ) { params: SetOf2DParametersWithVelocity -> gameMap.createTileTraverser(params) }

        TempSprites.init(renderProjection)

        val centerPoint = characterParameters.getCenterPoint()
        MatrixState.translate(
                screenWidth / 2 - centerPoint.x,
                screenHeight / 2 - centerPoint.y
        )

        return mutableListOf(gameMap, campfire, character).also { it.addAll(listOfNpc) }
    }

    private fun initGoblins(
            renderProjection: Matrix4f,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            boxInteractionContext: BoxInteractionContext,
            creator: (SetOf2DParametersWithVelocity) -> TileTraverser
    ): List<NpcEnemy> {
        val out = mutableListOf<NpcEnemy>()

        val enemy1 = NpcEnemy(goblinParams1)
                .also { npc ->
                    npc.init(
                            renderProjection,
                            boundingBoxCollisionContext,
                            boxInteractionContext,
                            creator(goblinParams1)
                    )
                }
        val enemy2 = NpcEnemy(goblinParams2)
                .also { npc ->
                    npc.init(
                            renderProjection,
                            boundingBoxCollisionContext,
                            boxInteractionContext,
                            creator(goblinParams2)
                    )
                }

        out.add(enemy1)
        out.add(enemy2)

        return out
    }
}