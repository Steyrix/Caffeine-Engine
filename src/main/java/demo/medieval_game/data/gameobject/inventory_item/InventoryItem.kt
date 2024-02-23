package demo.medieval_game.data.gameobject.inventory_item

import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.feature.animation.Animation

abstract class InventoryItem : CompositeEntity() {

    protected abstract val drawableComponent: Model

    abstract val canBeEquiped: Boolean

    abstract val isConsumable: Boolean

    abstract val onUseAnimations: List<Animation>

    abstract val onEquipAnimations: List<Animation>

    fun getDrawable(): Model {
        return drawableComponent
    }
}