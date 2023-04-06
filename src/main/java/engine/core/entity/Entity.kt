package engine.core.entity

interface Entity {
    fun onAdd() {}

    fun consumeInteraction(interaction: Interaction) {}

    fun getInteraction(): Interaction? = null
}