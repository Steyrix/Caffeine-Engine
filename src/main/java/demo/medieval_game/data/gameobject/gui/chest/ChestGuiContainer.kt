package demo.medieval_game.data.gameobject.gui.chest

import demo.medieval_game.ShaderController
import demo.medieval_game.data.gameobject.gui.button.GenericButton
import demo.medieval_game.data.gameobject.inventory_item.InventoryItemWrapper
import engine.core.controllable.Controllable
import engine.core.entity.CompositeEntity
import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class ChestGuiContainer(
    val parameters: SetOfStatic2DParameters
) : CompositeEntity(), Controllable {

    companion object {
        private const val COLUMN_COUNT = 7
        private const val ROW_COUNT = 5
        private const val CELLS_COUNT = 35
    }

    private var containerModel: Model? = null
    private val closeButton: GenericButton
    private val takeAllButton: GenericButton

    private val grid: Array<IntArray> = Array(COLUMN_COUNT) { IntArray(ROW_COUNT) }

    private val closeButtonParams = SetOfStatic2DParameters(
        x = 0f,
        y = 0f,
        xSize = parameters.xSize * 0.11f,
        ySize = parameters.ySize * 0.091f,
        rotationAngle = 0f
    )

    private val takeButtonParams = SetOfStatic2DParameters(
        x = 0f,
        y = 0f,
        xSize = parameters.xSize * 0.3f,
        ySize = parameters.ySize * 0.1155f,
        rotationAngle = 0f
    )

    init {
        closeButton = createCloseButtonModel()
        takeAllButton = createTakeButtonModel()
    }

    fun init(renderProjection: Matrix4f) {
        containerModel = createContainerModel(renderProjection)
        addComponent(containerModel!!, parameters)
        addComponent(closeButton, parameters)
        addComponent(takeAllButton, parameters)
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

        return GenericButton(
            onClick = { println("onClick close!") },
            texturePath = texturePath,
            parameters = closeButtonParams
        )
    }

    private fun createTakeButtonModel(): GenericButton {
        val texturePath = this.javaClass.getResource("/textures/gui/chest/ButtonTakeAll.png")!!.path

        return GenericButton(
            onClick = { println("onClick take all!") },
            texturePath = texturePath,
            parameters = takeButtonParams
        )
    }

    fun setContent(content: MutableList<InventoryItemWrapper>) {
        var id = 1
        content.forEach {
            // TODO: place in grid
            val itemSize = it.horizontalCellCount * it.verticalCellCount

            // grid.find(itemSize): List<Int>

            it.updateParameters(parameters)
            addComponent(it, it.parameters)
        }
    }

    fun updatePosition(pos: Point2D) {
        parameters.x = pos.x
        parameters.y = pos.y

        closeButtonParams.x = parameters.x + 0.726f * parameters.xSize
        closeButtonParams.y = parameters.y + 0.966f * parameters.ySize
        closeButton.updatePosition(Point2D(closeButtonParams.x, closeButtonParams.y))

        takeButtonParams.x = parameters.x + 0.13f * parameters.xSize
        takeButtonParams.y = parameters.y + 0.993f * parameters.ySize
        takeAllButton.updatePosition(Point2D(takeButtonParams.x, takeButtonParams.y))
    }

    fun setOnCloseClick(func: (Any?) -> Unit) {
        closeButton.updateOnClick(func)
    }

    fun setOnTakeClick(func: (Any?) -> Unit) {
        takeAllButton.updateOnClick(func)
    }

    fun IntArray.findEmptyHorizontalSpaceOf(size: Int): List<Int> {
        this.forEachIndexed { index, it ->
            if (it == 0) {
                if (index + 2 < this.size) {
                    if (this[index + 1] == 0 && this[index + 2] == 0)
                        return mutableListOf(index, index + 1, index + 2)
                }
            }
        }

        return emptyList()
    }

    fun Array<IntArray>.findEmptyHorizontalSpaceOf(size: Int): Pair<Int, List<Int>> {
        this.forEachIndexed { index, it ->
            val res = it.findEmptyHorizontalSpaceOf(size)
            if (res.isNotEmpty()) return Pair(index, res)
        }

        return Pair(-1, emptyList())
    }
}