package demo.labyrinth.goblin

import demo.labyrinth.hp.HealthBar
import demo.labyrinth.interaction.AttackInteraction
import engine.core.entity.CompositeEntity
import engine.core.loop.AccumulatedTimeEvent
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.getCenterPoint
import engine.feature.collision.CollisionReactive
import engine.feature.interaction.Interaction
import engine.feature.tiled.traversing.TileTraverser

class Goblin(
        private val drawableComponent: AnimatedObject2D,
        params: SetOf2DParametersWithVelocity,
        private val tileTraverser: TileTraverser,
        private val playerParams: SetOf2DParametersWithVelocity,
        private val hp: HealthBar
) : CompositeEntity(), CollisionReactive {

    private val startChasing = AccumulatedTimeEvent(
            timeLimit = 2f,
            action = {
                tileTraverser.moveTo(playerParams.getCenterPoint())
            }
    )

    private val controller = GoblinController(
            params,
            modifier = 20f,
    )

    init {
        addComponent(
                component = drawableComponent,
                parameters = params
        )

        addComponent(
                component = controller,
                parameters = params
        )

        addComponent(
                component = tileTraverser,
                parameters = params
        )

        addComponent(
                component = hp,
                parameters = params
        )
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        startChasing.schedule(deltaTime)
        drawableComponent.setAnimationByKey(controller.getAnimationKey())
    }

    override fun reactToCollision() {
        // TODO: implement
    }

    override fun consumeInteraction(interaction: Interaction) {
        when(interaction) {
            is AttackInteraction -> hp.filled -= interaction.damage
        }
    }
}