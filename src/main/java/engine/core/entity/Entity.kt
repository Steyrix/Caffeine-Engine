package engine.core.entity

import engine.feature.interaction.Interaction

interface Entity {

    fun onAdd() {}

    fun consumeInteraction(interaction: Interaction) {}

    fun getInteractions(): List<Interaction> = emptyList()

    fun onInteractionAvailable(producer: Entity) {}

    fun onInteractionUnavailable(producer: Entity) {}
}