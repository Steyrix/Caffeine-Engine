package demo.medieval_game.interaction

import engine.feature.interaction.Interaction

sealed class ChestInteraction : Interaction {
    object OpenClose : ChestInteraction()

    object Open : ChestInteraction()

    object Close : ChestInteraction()

    object Highlight : ChestInteraction()
}