package demo.medieval_game.data.starting_level

import demo.medieval_game.data.campfireParameters
import demo.medieval_game.data.characterParameters
import demo.medieval_game.data.gameobject.*
import demo.medieval_game.data.gameobject.npc.goblin.GoblinNPC
import demo.medieval_game.data.gameobject.npc.goblin.GoblinPreset
import demo.medieval_game.data.goblinParams1
import demo.medieval_game.data.goblinParams2
import engine.core.game_object.GameObject
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
    ): MutableList<out GameObject> {
        val campfire = Campfire(
                parameters = campfireParameters
        ).apply {
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
    ): List<GameObject> {
        val out = mutableListOf<GameObject>()
        val paramsList = listOf(goblinParams1, goblinParams2)

        paramsList.forEach {
            val goblin = createGoblinNPC(
                    renderProjection,
                    boundingBoxCollisionContext,
                    boxInteractionContext,
                    it,
                    creator,
                    tempSpritesHolder
            )

            out.add(goblin)
            goblin.spawn(it)
        }

        return out
    }

    private fun createGoblinNPC(
            renderProjection: Matrix4f,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            boxInteractionContext: BoxInteractionContext,
            params: SetOf2DParametersWithVelocity,
            creator: (SetOf2DParametersWithVelocity) -> TileTraverser,
            tempSpritesHolder: TempSpritesHolder
    ): GoblinNPC {
        return GoblinNPC(
                params,
                creator.invoke(params),
                characterParameters,
                tempSpritesHolder
        ).also {
            it.init(
                    renderProjection,
                    boundingBoxCollisionContext,
                    boxInteractionContext,
                    GoblinPreset.value
            )
        }
    }
}