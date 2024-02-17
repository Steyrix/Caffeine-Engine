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

    companion object {
        private const val COLUMN_COUNT = 7
        private const val ROW_COUNT = 5
        private const val CELLS_COUNT = 35
    }

    private var graphicalComponent: Model? = null

    private val grid: Array<IntArray> = Array(COLUMN_COUNT) { IntArray(ROW_COUNT) }

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
        var id = 1
        content.forEach {
            // TODO: place in grid

            it.updateParameters(parameters)
            addComponent(it, it.parameters)
        }
    }
}