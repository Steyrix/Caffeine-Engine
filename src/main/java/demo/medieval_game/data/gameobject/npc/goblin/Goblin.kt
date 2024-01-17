package demo.medieval_game.data.gameobject.npc.goblin

import demo.medieval_game.Player
import demo.medieval_game.data.AnimationKey
import demo.medieval_game.data.gameobject.TempSpritesHolder
import demo.medieval_game.hp.HealthBar
import demo.medieval_game.interaction.AttackInteraction
import engine.core.entity.CompositeEntity
import engine.core.render.AnimatedModel2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.interaction.Interaction

// TODO: remove coupling with hb bar
// TODO: remove coupling with controller
class Goblin(
    private val drawableComponent: AnimatedModel2D,
    params: SetOf2DParametersWithVelocity,
    private val hp: HealthBar,
    private val tempSpritesHolder: TempSpritesHolder,
    private val controller: GoblinController
) : CompositeEntity() {

    var onDispose: () -> Unit = {}

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

        if (entitiesMap.containsKey(controller)) {
            drawableComponent.setAnimationByKey(controller.getAnimationKey())
        }

        if (hp.filled <= 0) {
            onDispose.invoke()
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