package demo.medieval_game.data.gameobject.inventory_item

import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters

object LootCreator {

    fun createBasicLoot(parameters: SetOfStatic2DParameters): MutableList<CompositeEntity> {
        val out = mutableListOf<CompositeEntity>()

        val sword = CompositeEntity().apply {
            addComponent(
                Model(
                    texture = Texture2D.createInstance("/textures/gui/item/weapon/WarriorSwordIcon.png")
                ),
                parameters
            )
        }

        val wrapper = InventoryItemWrapper(
            parameters,
            ItemRarity.COMMON,
            sword
        )

        out.add(wrapper)

        return out
    }
}