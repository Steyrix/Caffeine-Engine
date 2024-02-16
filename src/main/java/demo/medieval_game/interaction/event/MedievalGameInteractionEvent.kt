package demo.medieval_game.interaction.event

import engine.core.entity.Entity
import engine.core.geometry.Point2D
import engine.feature.interaction.broadcast.InteractionEvent

sealed interface MedievalGameInteractionEvent : InteractionEvent

data class Loot(
    val content: MutableList<Entity>,
    val pos: Point2D
) : MedievalGameInteractionEvent
