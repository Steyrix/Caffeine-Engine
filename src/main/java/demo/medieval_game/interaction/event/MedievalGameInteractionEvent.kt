package demo.medieval_game.interaction.event

import engine.core.entity.Entity
import engine.feature.interaction.broadcast.InteractionEvent

sealed interface MedievalGameInteractionEvent : InteractionEvent

data class Loot(
    val content: MutableList<Entity>
) : MedievalGameInteractionEvent
