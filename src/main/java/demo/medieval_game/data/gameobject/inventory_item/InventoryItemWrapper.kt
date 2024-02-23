package demo.medieval_game.data.gameobject.inventory_item

import demo.medieval_game.ShaderController
import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class InventoryItemWrapper(
    val parameters: SetOfStatic2DParameters,
    rarity: ItemRarity,
    val item: InventoryItem,
    val horizontalCellCount: Int = 1,
    val verticalCellCount: Int = 1,
    val isAlignedHorizontally: Boolean = false
) : CompositeEntity() {

    private var backgroundGraphicalComponent: Model? = null

    init {
        val bgTexturePath = rarity.getResourcePath()
        backgroundGraphicalComponent = Model(
            texture = Texture2D.createInstance(bgTexturePath)
        )

        addComponent(backgroundGraphicalComponent!!, parameters)
        addComponent(item, parameters)
    }

    fun init(renderProjection: Matrix4f) {
        backgroundGraphicalComponent?.apply {
            shader = ShaderController.createTexturedShader(renderProjection)
            isPartOfWorldTranslation = false
            zLevel = 0f
        }
    }

    fun updateParameters(
        srcParams: SetOfStatic2DParameters,
        horizontalModifier: Float = 0f,
        verticalModifier: Float = 0f,
        horizontalCellBorderSize: Float,
        verticalCellBorderSize: Float
    ) {
        this.parameters.apply {
            x = srcParams.x + 0.13f * srcParams.xSize
            y = srcParams.y + 0.306f * srcParams.ySize
            xSize = srcParams.xSize * 0.1f
            ySize = srcParams.ySize * 0.1909f
        }

        this.parameters.apply {
            x += xSize * horizontalModifier + (horizontalModifier * horizontalCellBorderSize)
            y += ySize * verticalModifier + (verticalModifier * verticalCellBorderSize)
        }
    }
}