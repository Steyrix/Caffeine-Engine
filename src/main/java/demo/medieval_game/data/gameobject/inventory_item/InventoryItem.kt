package demo.medieval_game.data.gameobject.inventory_item

import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.feature.animation.Animation

abstract class InventoryItem : CompositeEntity() {

    protected abstract val drawableComponent: Model

    abstract val canBeEquiped: Boolean

    abstract val isConsumable: Boolean

    abstract val onUseAnimations: List<Animation>

    abstract val afterEquipAnimation: List<Animation>

    abstract val rarity: ItemRarity

    fun getDrawable(): Model {
        return drawableComponent
    }
}