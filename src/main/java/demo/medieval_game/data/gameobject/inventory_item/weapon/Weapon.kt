package demo.medieval_game.data.gameobject.inventory_item.weapon

import demo.medieval_game.data.gameobject.inventory_item.InventoryItem

abstract class Weapon : InventoryItem() {

    override val canBeEquiped = true

    override val isConsumable = false

    abstract val baseDamage: Float
}