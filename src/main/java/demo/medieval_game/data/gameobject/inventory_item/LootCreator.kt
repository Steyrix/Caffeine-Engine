package demo.medieval_game.data.gameobject.inventory_item

import demo.medieval_game.data.gameobject.inventory_item.weapon.ShortSword
import demo.medieval_game.scene.MedievalGame
import engine.core.update.SetOfStatic2DParameters

object LootCreator {

    fun createBasicLoot(parameters: SetOfStatic2DParameters): MutableList<InventoryItemWrapper> {
        val params = parameters.copy()
        val out = mutableListOf<InventoryItemWrapper>()

        val path = this.javaClass.getResource("/textures/gui/item/weapon/WarriorSwordIcon.png")!!.path

        val sword = object : ShortSword(
            texturePath = path,
            baseDamage = 100f,
            rarity = ItemRarity.COMMON,
            afterEquipAnimation = emptyList(),
            parameters = params
        ) {}

        val wrapper = InventoryItemWrapper(
            params,
            ItemRarity.COMMON,
            sword,
            1,
            2
        ).also {
            it.init(MedievalGame.renderProjection)
        }

        out.add(wrapper)

        return out
    }
}