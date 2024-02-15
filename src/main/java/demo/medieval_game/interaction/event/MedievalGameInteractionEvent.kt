package demo.medieval_game.interaction.event

import engine.core.entity.Entity

sealed interface MedievalGameInteractionEvent

data class Loot(
    val content: MutableList<Entity>
) : MedievalGameInteractionEvent
