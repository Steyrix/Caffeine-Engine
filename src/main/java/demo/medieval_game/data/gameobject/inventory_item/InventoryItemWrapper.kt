package demo.medieval_game.data.gameobject.inventory_item

import demo.medieval_game.ShaderController
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class InventoryItemWrapper(
    val parameters: SetOfStatic2DParameters,
    rarity: ItemRarity,
    private val item: Entity,
    val horizontalCellCount: Int = 1,
    val verticalCellCount: Int = 1
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
        parameters: SetOfStatic2DParameters,
        horizontalModifier: Float = 1f,
        verticalModifier: Float = 1f
    ) {
        this.parameters.apply {
            x = parameters.x + 0.13f * parameters.xSize * horizontalModifier
            y = parameters.y + 0.306f * parameters.ySize * verticalModifier
            xSize = parameters.xSize * 0.1f
            ySize = parameters.ySize * 0.1909f
        }
    }
}