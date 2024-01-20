package demo.medieval_game.data.gameobject.npc.goblin

import demo.medieval_game.Player
import demo.medieval_game.data.gameobject.TempSpritesHolder
import demo.medieval_game.interaction.AttackInteraction
import demo.medieval_game.interaction.PlayerAttackInteraction
import engine.core.entity.CompositeEntity
import engine.core.render.AnimatedModel2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.getCenterPoint
import engine.feature.interaction.Interaction

class Goblin(
    private val drawableComponent: AnimatedModel2D,
    private val parameters: SetOf2DParametersWithVelocity,
    private val tempSpritesHolder: TempSpritesHolder
) : CompositeEntity() {

    init {
        addComponent(
            component = drawableComponent,
            parameters = parameters
        )

        addComponent(
            component = tempSpritesHolder,
            parameters = parameters
        )
    }

    override fun consumeInteraction(interaction: Interaction) {
        if (isDisposed) return

        when (interaction) {
            is PlayerAttackInteraction -> {
                tempSpritesHolder.generateHit(
                    parameters.x,
                    parameters.y,
                    posZ = drawableComponent.zLevel + 0.5f
                )
            }
        }

        super.consumeInteraction(interaction)
    }

    override fun getInteractions(): List<Interaction> {
        val out = mutableListOf<Interaction>()

        if (!isDisposed) {
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