package demo.medieval_game.data.gameobject.inventory_item

import engine.feature.animation.Animation

interface InventoryItem {

    val isEquipable: Boolean

    val isConsumable: Boolean

    val onUseAnimations: List<Animation>

    val onEquipAnimations: List<Animation>
}