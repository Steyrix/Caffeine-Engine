package demo.medieval_game.data.gameobject.npc.goblin

import demo.medieval_game.Player
import demo.medieval_game.data.AnimationKey
import demo.medieval_game.data.gameobject.TempSpritesHolder
import demo.medieval_game.hp.HealthBar
import demo.medieval_game.interaction.AttackInteraction
import demo.medieval_game.interaction.IsAttackableInteraction
import engine.core.entity.CompositeEntity
import engine.core.loop.PredicateTimeEvent
import engine.core.render.AnimatedModel2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.interaction.Interaction
import engine.feature.tiled.traversing.TileTraverser

class Goblin(
    private val drawableComponent: AnimatedModel2D,
    params: SetOf2DParametersWithVelocity,
    private val tileTraverser: TileTraverser,
    private val hp: HealthBar,
    private val tempSpritesHolder: TempSpritesHolder
) : CompositeEntity() {

    private val startChasing = PredicateTimeEvent(
        timeLimit = 2f,
        predicate = { entitiesMap.containsKey(tileTraverser) },
        action = {
            tileTraverser.moveToTarget()
        }
    )

    private var isMovingStopped = false

    private val suspendMove = PredicateTimeEvent(
        timeLimit = 1.5f,
        predicate = { isMovingStopped },
        action = {
            tileTraverser.resume()
        }
    )

    private val controller = GoblinController(
        params,
        modifier = 20f
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

        addComponent(
            component = tempSpritesHolder,
            parameters = params
        )
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        startChasing.schedule(deltaTime)
        suspendMove.schedule(deltaTime)

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

        when (interaction) {
            is AttackInteraction -> {
                if (interaction.producer !is Player) return
                val currPos = controller.getCurrentCenterPos()
                tempSpritesHolder.generateHit(
                    currPos.x,
                    currPos.y,
                    posZ = drawableComponent.zLevel + 0.5f
                )
            }
            is IsAttackableInteraction -> {
                if (!isMovingStopped) {
                    tileTraverser.pause()
                    isMovingStopped = true
                }
            }
        }

        super.consumeInteraction(interaction)
    }

    override fun getInteractions(): List<Interaction> {
        val out = mutableListOf<Interaction>()

        if (controller.isStriking && !isDisposed) {
            out.add(
                AttackInteraction(
                    producer = this,
                    damage = 0.05f
                )
            )
        }

        return out
    }
}