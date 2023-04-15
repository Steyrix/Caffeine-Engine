package demo.labyrinth.goblin

import demo.labyrinth.data.AnimationKey
import demo.labyrinth.data.gameobject.TempSprites
import demo.labyrinth.hp.HealthBar
import demo.labyrinth.interaction.AttackInteraction
import demo.labyrinth.interaction.IsAttackableInteraction
import engine.core.controllable.Direction
import engine.core.entity.CompositeEntity
import engine.core.loop.PredicateTimeEvent
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.getCenterPoint
import engine.feature.collision.CollisionReactive
import engine.feature.interaction.Interaction
import engine.feature.tiled.traversing.TileTraverser
import kotlin.math.abs

class Goblin(
        private val drawableComponent: AnimatedObject2D,
        private val params: SetOf2DParametersWithVelocity,
        private val tileTraverser: TileTraverser,
        private val playerParams: SetOf2DParametersWithVelocity,
        private val hp: HealthBar
) : CompositeEntity(), CollisionReactive {

    private val startChasing = PredicateTimeEvent(
            timeLimit = 2f,
            predicate = { parametersMap.containsKey(tileTraverser) },
            action = {
                tileTraverser.moveTo(playerParams.getCenterPoint())
            }
    )

    private var isMovingStopped = false

    private val moveCooldown = PredicateTimeEvent(
            timeLimit = 1.5f,
            predicate = { isMovingStopped },
            action = {
                tileTraverser.resume()
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
        moveCooldown.schedule(deltaTime)

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
            is IsAttackableInteraction -> {
                if (!controller.isStriking) {
                    controller.strike(playerParams)
                    tileTraverser.pause()
                    isMovingStopped = true
                }
            }
        }
    }

    override fun getInteractions(): List<Interaction> {
        val out = mutableListOf<Interaction>()

        if (controller.isStriking) {
            out.add(AttackInteraction(damage = 0.05f))
        }

        return out
    }
}