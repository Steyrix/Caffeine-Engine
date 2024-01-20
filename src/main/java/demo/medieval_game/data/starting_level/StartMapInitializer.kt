package demo.medieval_game.data.starting_level

import demo.medieval_game.data.gameobject.*
import demo.medieval_game.data.gameobject.npc.goblin.GoblinNPC
import demo.medieval_game.data.gameobject.npc.goblin.GoblinPreset
import demo.medieval_game.data.gameobject.on_map.Campfire
import demo.medieval_game.data.gameobject.on_map.chest.ChestCreator
import demo.medieval_game.data.gameobject.on_map.chest.ChestType
import demo.medieval_game.data.static_parameters.*
import demo.medieval_game.scene.MedievalGame
import engine.core.game_object.GameEntity
import engine.core.game_object.SingleGameEntity
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

        val chestParametersList = listOf(
            woodenChestParameters,
            ironChestParameters,
            rustyIronChestParameters,
            blueChestParameters,
            greenChestParameters,
            purpleChestParameters
        )
        val chests = mutableListOf<SingleGameEntity>()
        ChestType.values().forEachIndexed { i, it ->
            chests.add(
                ChestCreator.create(
                    MedievalGame.renderProjection,
                    boxInteractionContext,
                    it,
                    chestParametersList[i]
                )
            )
        }

        val listOfNpc = initGoblins(
            boundingBoxCollisionContext,
            boxInteractionContext,
        ) { params: SetOf2DParametersWithVelocity -> tileTraverserCreator.invoke(params) }


        val out = mutableListOf<GameEntity>().apply {
            add(campfire)
            addAll(chests)
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