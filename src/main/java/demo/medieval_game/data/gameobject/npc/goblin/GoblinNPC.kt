package demo.medieval_game.data.gameobject.npc.goblin

import demo.medieval_game.data.gameobject.TempSpritesHolder
import demo.medieval_game.data.gameobject.npc.NPC
import engine.core.render.AnimatedModel2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.tiled.traversing.TileTraverser
import org.joml.Matrix4f

class GoblinNPC(
    params: SetOf2DParametersWithVelocity,
    private val tileTraverser: TileTraverser,
    private val tempSpritesHolder: TempSpritesHolder
) : NPC<Goblin>(params) {

    override fun initEntity(
        renderProjection: Matrix4f,
        animatedModel2D: AnimatedModel2D
    ): Goblin {

        val controller = GoblinController(
            parameters,
            modifier = 20f
        )

        val entity = Goblin(
            animatedModel2D,
            parameters,
            getHpBar(renderProjection),
            tempSpritesHolder,
            controller
        ).apply {
            addComponent(tileTraverser, parameters)
        }

        entity.onDispose = {
            entity.removeComponent(tileTraverser)
            entity.removeComponent(controller)
        }

        return entity
    }
}