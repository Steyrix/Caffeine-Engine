package demo.medieval_game.interaction

import engine.core.update.SetOfParameters
import engine.feature.interaction.Interaction

data class IsAttackableInteraction(
        val targetParams: SetOfParameters
) : Interaction