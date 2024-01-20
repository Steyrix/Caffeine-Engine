package demo.medieval_game

import demo.medieval_game.data.gameobject.TempSpritesHolder
import demo.medieval_game.interaction.AttackInteraction
import demo.medieval_game.interaction.ChestInteraction
import demo.medieval_game.interaction.IsAttackableInteraction
import engine.core.entity.CompositeEntity
import engine.core.loop.PredicateTimeEvent
import engine.core.render.AnimatedModel2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.interaction.Interaction

class Player(
    private val drawableComponent: AnimatedModel2D,
    private val parameters: SetOf2DParametersWithVelocity,
    private val tempSpritesHolder: TempSpritesHolder
) : CompositeEntity() {

    private var isAttack = false
    var isStriking = false

    private var isChestInteraction = false
    var isInteractingWithChest = false

    private val attackCoolDown = PredicateTimeEvent(
        timeLimit = 0.5f,
        predicate = { isAttack },
        action = {
            isAttack = false
        }
    )

    private val chestInteractionCooldown = PredicateTimeEvent(
        timeLimit = 0.5f,
        predicate = { isChestInteraction },
        action = {
            isChestInteraction = false
            isInteractingWithChest = false
        }
    )

    private var isHit = false

    private val takeHitCoolDown = PredicateTimeEvent(
        timeLimit = 0.5f,
        predicate = { isHit },
        action = {
            isHit = false
        }
    )

    init {
        addComponent(
            component = drawableComponent,
            parameters = parameters
        )
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        attackCoolDown.schedule(deltaTime)
        takeHitCoolDown.schedule(deltaTime)
        chestInteractionCooldown.schedule(deltaTime)
    }

    override fun getInteractions(): List<Interaction> {
        val out = mutableListOf<Interaction>()
        if (isStriking && !isAttack) {
            isAttack = true
            out.add(AttackInteraction(this))
        }
        if (isInteractingWithChest && !isChestInteraction) {
            isChestInteraction = true
            out.add(ChestInteraction.OpenClose)
        }

        out.add(IsAttackableInteraction(parameters))
        return out.toList()
    }

    override fun consumeInteraction(interaction: Interaction) {
        when (interaction) {
            is AttackInteraction -> {
                if (!isHit) {
                    isHit = true
                    tempSpritesHolder.generateHit(
                        parameters.x,
                        parameters.y,
                        posZ = drawableComponent.zLevel + 0.5f
                    )
                }
            }
        }
    }
}