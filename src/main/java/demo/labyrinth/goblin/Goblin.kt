package demo.labyrinth.goblin

import demo.labyrinth.Player
import demo.labyrinth.data.AnimationKey
import demo.labyrinth.data.gameobject.TempSpritesHolder
import demo.labyrinth.hp.HealthBar
import demo.labyrinth.interaction.AttackInteraction
import demo.labyrinth.interaction.IsAttackableInteraction
import engine.core.entity.CompositeEntity
import engine.core.loop.PredicateTimeEvent
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.getCenterPoint
import engine.feature.interaction.Interaction
import engine.feature.tiled.traversing.TileTraverser

class Goblin(
        private val drawableComponent: AnimatedObject2D,
        params: SetOf2DParametersWithVelocity,
        private val tileTraverser: TileTraverser,
        private val playerParams: SetOf2DParametersWithVelocity,
        private val hp: HealthBar,
        private val tempSpritesHolder: TempSpritesHolder
) : CompositeEntity() {

    private val startChasing = PredicateTimeEvent(
            timeLimit = 2f,
            predicate = { entitiesMap.containsKey(tileTraverser) },
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

        if (entitiesMap.containsKey(controller)) {
            drawableComponent.setAnimationByKey(controller.getAnimationKey())
        }

        if (hp.filled <= 0) {
            entitiesMap.remove(tileTraverser)
            entitiesMap.remove(controller)
            drawableComponent.setAnimationByKey(AnimationKey.GOBLIN_DEFEAT)
            isDisposed = true
            return
        }
    }

    override fun consumeInteraction(interaction: Interaction) {
        if (isDisposed) return

        when(interaction) {
            is AttackInteraction -> {
                if (interaction.producer !is Player) return
                val currPos = controller.getCurrentCenterPos()
                tempSpritesHolder.generateHit(currPos.x, currPos.y)
            }
            is IsAttackableInteraction -> {
                if (!isMovingStopped) {
                    tileTraverser.pause()
                    isMovingStopped = true
                }
            }
        }

        entitiesMap.keys.forEach {
            it.consumeInteraction(interaction)
        }
    }

    override fun getInteractions(): List<Interaction> {
        val out = mutableListOf<Interaction>()

        if (controller.isStriking) {
            out.add(AttackInteraction(
                    producer = this,
                    damage = 0.05f
            ))
        }

        return out
    }
}