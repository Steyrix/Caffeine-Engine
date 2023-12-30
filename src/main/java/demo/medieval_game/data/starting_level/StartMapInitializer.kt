package demo.medieval_game.data.starting_level

import demo.medieval_game.data.campfireParameters
import demo.medieval_game.data.gameobject.*
import demo.medieval_game.data.gameobject.npc.goblin.GoblinNPC
import demo.medieval_game.data.gameobject.npc.goblin.GoblinPreset
import demo.medieval_game.data.goblinParams1
import demo.medieval_game.data.goblinParams2
import demo.medieval_game.scene.MedievalGame
import engine.core.game_object.GameEntity
import engine.core.scene.SceneInitializer
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.tiled.traversing.TileTraverser

object StartMapInitializer : SceneInitializer {
    fun initAll(
        boundingBoxCollisionContext: BoundingBoxCollisionContext,
        boxInteractionContext: BoxInteractionContext,
        tileTraverserCreator: (SetOf2DParametersWithVelocity) -> TileTraverser
    ): MutableList<out GameEntity> {
        val campfire = Campfire(
            parameters = campfireParameters
        ).apply {
            init(MedievalGame.renderProjection)
        }

        val listOfNpc = initGoblins(
            boundingBoxCollisionContext,
            boxInteractionContext,
        ) { params: SetOf2DParametersWithVelocity -> tileTraverserCreator.invoke(params) }

        val out = mutableListOf<GameEntity>().apply {
            add(campfire)
            addAll(listOfNpc)
        }

        return out
    }

    private fun initGoblins(
        boundingBoxCollisionContext: BoundingBoxCollisionContext,
        boxInteractionContext: BoxInteractionContext,
        creator: (SetOf2DParametersWithVelocity) -> TileTraverser
    ): List<GameEntity> {
        val out = mutableListOf<GameEntity>()
        val paramsList = listOf(goblinParams1, goblinParams2)

        paramsList.forEach {
            val goblin = GoblinNPC(
                it,
                creator.invoke(it),
                TempSpritesHolder().apply { init(MedievalGame.renderProjection) }
            ).also { npc ->
                npc.init(
                    MedievalGame.renderProjection,
                    boundingBoxCollisionContext,
                    boxInteractionContext,
                    GoblinPreset.get()
                )
            }

            out.add(goblin)
            goblin.spawn(it)
        }

        return out
    }
}