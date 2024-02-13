package demo.medieval_game.interaction

import engine.feature.interaction.Interaction

sealed class ChestInteraction : Interaction {
    object OpenClose : ChestInteraction()

    object Highlight : ChestInteraction()
}