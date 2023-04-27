package demo.medieval_game.interaction

import engine.core.entity.CompositeEntity
import engine.feature.interaction.Interaction

data class AttackInteraction(
        val producer: CompositeEntity,
        val damage: Float = 0.25f
) : Interaction