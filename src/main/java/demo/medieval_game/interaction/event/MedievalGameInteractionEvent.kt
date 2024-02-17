package demo.medieval_game.interaction.event

import demo.medieval_game.data.gameobject.inventory_item.InventoryItemWrapper
import engine.core.geometry.Point2D
import engine.feature.interaction.broadcast.InteractionEvent

sealed interface MedievalGameInteractionEvent : InteractionEvent

data class OpenChest(
    val content: MutableList<InventoryItemWrapper>,
    val pos: Point2D
) : MedievalGameInteractionEvent

object CloseChest : MedievalGameInteractionEvent
