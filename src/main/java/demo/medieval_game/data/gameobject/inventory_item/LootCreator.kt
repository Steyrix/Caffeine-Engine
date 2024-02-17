package demo.medieval_game.data.gameobject.inventory_item

import demo.medieval_game.ShaderController
import demo.medieval_game.scene.MedievalGame
import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters

object LootCreator {

    fun createBasicLoot(parameters: SetOfStatic2DParameters): MutableList<InventoryItemWrapper> {
        val out = mutableListOf<InventoryItemWrapper>()

        val sword = CompositeEntity().apply {

            val path = this.javaClass.getResource("/textures/gui/item/weapon/WarriorSwordIcon.png")!!.path

            addComponent(
                Model(
                    texture = Texture2D.createInstance(path)
                ).also {
                       it.shader = ShaderController.createTexturedShader(MedievalGame.renderProjection)
                },
                parameters
            )
        }

        val wrapper = InventoryItemWrapper(
            parameters,
            ItemRarity.COMMON,
            sword
        ).also {
            it.init(MedievalGame.renderProjection)
        }

        out.add(wrapper)

        return out
    }
}