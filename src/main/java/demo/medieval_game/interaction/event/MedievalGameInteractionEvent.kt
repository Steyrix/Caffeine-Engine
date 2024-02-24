package demo.medieval_game.interaction.event

import demo.medieval_game.data.gameobject.inventory_item.InventoryItemWrapper
import engine.feature.interaction.broadcast.InteractionEvent

sealed interface MedievalGameInteractionEvent : InteractionEvent

data class OpenChest(
    val content: MutableList<InventoryItemWrapper>
) : MedievalGameInteractionEvent

object CloseChest : MedievalGameInteractionEvent
