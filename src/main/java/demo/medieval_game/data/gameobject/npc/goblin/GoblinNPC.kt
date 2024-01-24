package demo.medieval_game.data.gameobject.npc.goblin

import demo.medieval_game.data.AnimationKey
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
            animatedModel2D,
            parameters,
            modifier = 20f
        )

        val entity = Goblin(
            animatedModel2D,
            parameters,
            tempSpritesHolder
        )

        val hpBar = getHpBar(
            renderProjection
        ) {
            animatedModel2D.setAnimationByKey(AnimationKey.GOBLIN_DEFEAT)
            entity.removeComponent(tileTraverser)
            entity.removeComponent(controller)
            entity.isDisposed = true
        }

        entity.addComponent(hpBar, parameters)
        entity.addComponent(tileTraverser, parameters)
        entity.addComponent(controller, parameters)

        return entity
    }
}