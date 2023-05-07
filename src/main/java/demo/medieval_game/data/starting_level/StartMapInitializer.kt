package demo.medieval_game.data.starting_level

import demo.medieval_game.data.gameobject.*
import demo.medieval_game.data.goblinParams1
import demo.medieval_game.data.goblinParams2
import engine.core.scene.GameObject
import engine.core.scene.SceneInitializer
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.tiled.traversing.TileTraverser
import org.joml.Matrix4f

object StartMapInitializer : SceneInitializer {
    fun initAll(
            renderProjection: Matrix4f,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            boxInteractionContext: BoxInteractionContext,
            tempSpritesHolder: TempSpritesHolder,
            tileTraverserCreator: (SetOf2DParametersWithVelocity) -> TileTraverser
    ): MutableList<GameObject> {
        val campfire = Campfire().apply {
            init(renderProjection)
        }

        val listOfNpc = initGoblins(
                renderProjection,
                boundingBoxCollisionContext,
                boxInteractionContext,
                tempSpritesHolder
        ) { params: SetOf2DParametersWithVelocity -> tileTraverserCreator.invoke(params) }

        return mutableListOf(campfire, tempSpritesHolder).also { it.addAll(listOfNpc) }
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