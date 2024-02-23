package demo.medieval_game.data.gameobject.character

import demo.medieval_game.data.gameobject.inventory_item.InventoryItem
import engine.core.entity.CompositeEntity
import engine.core.render.AnimatedModel2D
import engine.core.render.Model
import engine.core.update.SetOfStatic2DParameters

class InventoryController(
    private val holder: AnimatedModel2D,
    private val container: Model,
    val parameters: SetOfStatic2DParameters
) : CompositeEntity() {

    private val items = mutableListOf<InventoryItem>()

    init {
        addComponent(container, parameters)
    }

    fun addToInventory(items: MutableList<InventoryItem>) {
        this.items.addAll(items)
        items.forEach {
            modifyParameters(it)
            addComponent(it, it.parameters)
        }
    }

    fun onItemEquip(item: InventoryItem) {
        if (item.canBeEquipped) {
            holder.changeAnimationSet(item.afterEquipAnimation)
        }
    }

    fun modifyParameters(item: InventoryItem) {

    }
}