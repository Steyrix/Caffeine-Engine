package engine.core.entity

interface Entity {
    fun onAdd() {}

    fun consumeInteraction(interaction: Interaction) {}
}