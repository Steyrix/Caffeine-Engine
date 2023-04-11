package demo.labyrinth.interaction

import engine.feature.interaction.Interaction

data class AttackInteraction(
        val damage: Float = 0.25f
) : Interaction