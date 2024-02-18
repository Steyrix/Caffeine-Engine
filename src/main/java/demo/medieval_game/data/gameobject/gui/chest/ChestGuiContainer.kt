package demo.medieval_game.data.gameobject.gui.chest

import demo.medieval_game.ShaderController
import demo.medieval_game.data.gameobject.gui.button.GenericButton
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

    private var containerModel: Model? = null
    private var closeButtonModel: GenericButton? = null
    private var takeAllButtonModel: Model? = null

    private val grid: Array<IntArray> = Array(COLUMN_COUNT) { IntArray(ROW_COUNT) }

    fun init(renderProjection: Matrix4f) {
        containerModel = createContainerModel(renderProjection)
        closeButtonModel = createCloseButtonModel()
        //takeAllButtonModel = createTakeButtonModel()

        addComponent(containerModel!!, parameters)
        addComponent(closeButtonModel!!, parameters)
    }

    private fun createContainerModel(renderProjection: Matrix4f): Model {
        val texturePath = this.javaClass.getResource("/textures/gui/chest/OpenedChestGui.png")!!.path

        return Model(
            texture = Texture2D.createInstance(texturePath)
        ).apply {
            shader = ShaderController.createTexturedShader(renderProjection)
            zLevel = Float.NEGATIVE_INFINITY
            isPartOfWorldTranslation = false
        }
    }

    private fun createCloseButtonModel(): GenericButton {
        val texturePath = this.javaClass.getResource("/textures/gui/chest/ButtonClose.png")!!.path

        val closeButtonParams = SetOfStatic2DParameters(
            x = parameters.x + 0.726f * parameters.xSize,
            y = parameters.y + 0.966f * parameters.ySize,
            xSize = parameters.xSize * 0.11f,
            ySize = parameters.ySize * 0.103f,
            rotationAngle = 0f
        )

        return GenericButton(
            onClick = { println("onClick close!") },
            texturePath = texturePath,
            parameters = closeButtonParams
        )
    }

    private fun createTakeButtonModel(): GenericButton {
        val texturePath = this.javaClass.getResource("/textures/gui/chest/ButtonTakeAll.png")!!.path

        return GenericButton(
            onClick = { println("onClick close!") },
            texturePath = texturePath,
            parameters = parameters
        )
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