package demo.medieval_game.data.gameobject.npc.goblin

import demo.medieval_game.data.gameobject.TempSpritesHolder
import demo.medieval_game.data.gameobject.npc.NPC
import demo.medieval_game.hp.HealthBar
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.traversing.TileTraverser
import org.joml.Matrix4f

class GoblinNPC(
        params: SetOf2DParametersWithVelocity,
        private val tileTraverser: TileTraverser,
        private val playerParams: SetOf2DParametersWithVelocity,
        private val tempSpritesHolder: TempSpritesHolder
) : NPC<Goblin>(params) {

    override fun initEntity(
            renderProjection: Matrix4f,
            animatedObject2D: AnimatedObject2D
    ): Goblin {
        return Goblin(
                animatedObject2D,
                parameters,
                tileTraverser,
                playerParams,
                getGoblinHealthBar(renderProjection),
                tempSpritesHolder
        )
    }

    private fun getGoblinHealthBar(renderProjection: Matrix4f): HealthBar {
        return HealthBar(
                parameters,
                SetOfStatic2DParameters(
                        x = 0f,
                        y = 0f,
                        xSize = 50f,
                        ySize = 12.5f,
                        rotationAngle = 0f
                ),
                renderProjection
        )
    }
}