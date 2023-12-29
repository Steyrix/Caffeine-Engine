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
            tempSpritesHolder: TempSpritesHolder,
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
                tempSpritesHolder
        ) { params: SetOf2DParametersWithVelocity -> tileTraverserCreator.invoke(params) }

        return mutableListOf(campfire, tempSpritesHolder).also { it.addAll(listOfNpc) }
    }

    private fun initGoblins(
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            boxInteractionContext: BoxInteractionContext,
            tempSpritesHolder: TempSpritesHolder,
            creator: (SetOf2DParametersWithVelocity) -> TileTraverser
    ): List<GameEntity> {
        val out = mutableListOf<GameEntity>()
        val paramsList = listOf(goblinParams1, goblinParams2)

        paramsList.forEach {
            val goblin = GoblinNPC(
                    it,
                    creator.invoke(it),
                    tempSpritesHolder
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