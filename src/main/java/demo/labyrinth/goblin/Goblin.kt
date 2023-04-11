package demo.labyrinth.goblin

import demo.labyrinth.data.AnimationKey
import demo.labyrinth.data.gameobject.TempSprites
import demo.labyrinth.hp.HealthBar
import demo.labyrinth.interaction.AttackInteraction
import engine.core.entity.CompositeEntity
import engine.core.loop.PredicateTimeEvent
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.getCenterPoint
import engine.feature.collision.CollisionReactive
import engine.feature.interaction.Interaction
import engine.feature.tiled.traversing.TileTraverser
import kotlin.math.pow
import kotlin.math.sqrt

class Goblin(
        private val drawableComponent: AnimatedObject2D,
        private val params: SetOf2DParametersWithVelocity,
        private val tileTraverser: TileTraverser,
        private val playerParams: SetOf2DParametersWithVelocity,
        private val hp: HealthBar
) : CompositeEntity(), CollisionReactive {

    private val distanceToStrike = 15f

    private val startChasing = PredicateTimeEvent(
            timeLimit = 2f,
            predicate = { parametersMap.containsKey(tileTraverser) },
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

        if (parametersMap.containsKey(controller)) {
            drawableComponent.setAnimationByKey(controller.getAnimationKey())
        }

        if (hp.filled <= 0) {
            parametersMap.remove(tileTraverser)
            parametersMap.remove(controller)
            drawableComponent.setAnimationByKey(AnimationKey.GOBLIN_DEFEAT)
            isDisposed = true
            return
        }

        if (getDistanceToPlayer() <= distanceToStrike) {
            controller.strike()
        }
    }

    override fun reactToCollision() {
        // TODO: implement
    }

    override fun consumeInteraction(interaction: Interaction) {
        when(interaction) {
            is AttackInteraction -> {
                val currPos = controller.getCurrentCenterPos()
                TempSprites.generateHit(currPos.x, currPos.y)
                hp.filled -= interaction.damage
            }
        }
    }

    private fun getDistanceToPlayer(): Float {
        val x = (playerParams.x - params.x).toDouble().pow(2.0)
        val y = (playerParams.y - params.y).toDouble().pow(2.0)
        return sqrt(x.toFloat() + y.toFloat())
    }
}