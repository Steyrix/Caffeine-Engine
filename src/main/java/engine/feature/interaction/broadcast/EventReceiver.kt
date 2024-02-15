package engine.feature.interaction.broadcast

import engine.feature.interaction.Interaction

interface EventReceiver {

    fun proccessEvent(interaction: Interaction)
}