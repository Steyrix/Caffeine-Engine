package demo.medieval_game.data.gameobject.gui.chest

import demo.medieval_game.ShaderController
import demo.medieval_game.data.gameobject.inventory_item.InventoryItemWrapper
import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class ChestGuiContainer(
    val parameters: SetOfStatic2DParameters
) : CompositeEntity() {

    private var graphicalComponent: Model? = null

    fun init(renderProjection: Matrix4f) {
        val texturePath = this.javaClass.getResource("/textures/gui/chest/OpenedChestGui.png")!!.path

        graphicalComponent = Model(
            texture = Texture2D.createInstance(texturePath)
        ).apply {
            shader = ShaderController.createTexturedShader(renderProjection)
            zLevel = Float.NEGATIVE_INFINITY
            isPartOfWorldTranslation = false
        }

        addComponent(graphicalComponent!!, parameters)
    }

    fun setContent(content: MutableList<InventoryItemWrapper>) {
        content.forEach {
            addComponent(it, it.parameters)
        }
    }
}