package demo.medieval_game.data.gameobject.inventory_item

import demo.medieval_game.ShaderController
import demo.medieval_game.scene.MedievalGame
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters

object LootCreator {

    fun createBasicLoot(parameters: SetOfStatic2DParameters): MutableList<InventoryItemWrapper> {
        val params = parameters.copy()
        val out = mutableListOf<InventoryItemWrapper>()

        val path = this.javaClass.getResource("/textures/gui/item/weapon/WarriorSwordIcon.png")!!.path

        val sword = Model(
            texture = Texture2D.createInstance(path)
        ).also {
            it.shader = ShaderController.createTexturedShader(MedievalGame.renderProjection)
            it.zLevel = 1f
            it.isPartOfWorldTranslation = false
        }

        val wrapper = InventoryItemWrapper(
            params,
            ItemRarity.COMMON,
            sword,
            2
        ).also {
            it.init(MedievalGame.renderProjection)
        }

        out.add(wrapper)

        return out
    }
}