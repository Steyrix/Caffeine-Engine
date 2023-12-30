package demo.medieval_game

import demo.medieval_game.data.gameobject.TempSpritesHolder
import demo.medieval_game.interaction.AttackInteraction
import demo.medieval_game.interaction.IsAttackableInteraction
import engine.core.entity.CompositeEntity
import engine.core.loop.PredicateTimeEvent
import engine.core.render.AnimatedModel2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.interaction.Interaction

class Player(
    private val drawableComponent: AnimatedModel2D,
    private val params: SetOf2DParametersWithVelocity,
    private val tempSpritesHolder: TempSpritesHolder
) : CompositeEntity() {

    private var isAttack = false

    private val attackCoolDown = PredicateTimeEvent(
        timeLimit = 0.5f,
        predicate = { isAttack },
        action = {
            isAttack = false
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

    private val controller = SimpleController2D(
        params,
        absVelocityY = 10f,
        absVelocityX = 10f,
        modifier = 20f,
        isControlledByUser = true
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
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        drawableComponent.setAnimationByKey(controller.getAnimationKey())
        attackCoolDown.schedule(deltaTime)
        takeHitCoolDown.schedule(deltaTime)
    }

    override fun getInteractions(): List<Interaction> {
        val out = mutableListOf<Interaction>()
        if (controller.isStriking && !isAttack) {
            isAttack = true
            out.add(AttackInteraction(this))
        }

        out.add(IsAttackableInteraction(params))
        return out.toList()
    }

    fun getDirection() = controller.direction

    override fun consumeInteraction(interaction: Interaction) {
        when (interaction) {
            is AttackInteraction -> {
                if (!isHit) {
                    isHit = true
                    val currPos = controller.getCurrentCenterPos()
                    tempSpritesHolder.generateHit(currPos.x, currPos.y)
                    // hp.filled -= interaction.damage
                }
            }
        }
    }
}