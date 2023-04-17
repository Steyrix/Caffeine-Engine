package demo.labyrinth

import demo.labyrinth.data.gameobject.TempSprites
import demo.labyrinth.interaction.AttackInteraction
import demo.labyrinth.interaction.IsAttackableInteraction
import engine.core.entity.CompositeEntity
import engine.core.loop.PredicateTimeEvent
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.interaction.Interaction

class Player(
        private val drawableComponent: AnimatedObject2D,
        private val params: SetOf2DParametersWithVelocity
) : CompositeEntity() {

    private var isAttack = false

    private val attackCooldown = PredicateTimeEvent(
            timeLimit = 0.5f,
            predicate = { isAttack },
            action = {
                isAttack = false
            }
    )

    private var isHit = false

    private val takeHitCooldown = PredicateTimeEvent(
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
        attackCooldown.schedule(deltaTime)
        takeHitCooldown.schedule(deltaTime)
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

    override fun consumeInteraction(interaction: Interaction) {
        when(interaction) {
            is AttackInteraction -> {
                if (!isHit) {
                    isHit = true
                    val currPos = controller.getCurrentCenterPos()
                    TempSprites.generateHit(currPos.x, currPos.y)
                    // hp.filled -= interaction.damage
                }
            }
        }
    }
}