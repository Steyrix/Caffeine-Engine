package demo.medieval_game.data.gameobject.inventory_item

import engine.core.entity.CompositeEntity
import engine.feature.animation.Animation

abstract class InventoryItem : CompositeEntity() {

    abstract val isEquipable: Boolean

    abstract val isConsumable: Boolean

    abstract val onUseAnimations: List<Animation>

    abstract val onEquipAnimations: List<Animation>
}