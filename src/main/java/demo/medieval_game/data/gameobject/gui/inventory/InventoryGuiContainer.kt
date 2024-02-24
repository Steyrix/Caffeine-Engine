package demo.medieval_game.data.gameobject.gui.inventory

import demo.medieval_game.ShaderController
import demo.medieval_game.data.gameobject.gui.button.GenericButton
import engine.core.controllable.Controllable
import engine.core.entity.CompositeEntity
import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class InventoryGuiContainer(
    val parameters: SetOfStatic2DParameters
) : CompositeEntity(), Controllable {

    private var graphicalComponent: Model? = null
    private val closeButton: GenericButton

    private val closeButtonParams = SetOfStatic2DParameters(
        x = 0f,
        y = 0f,
        xSize = parameters.xSize * 0.11f,
        ySize = parameters.ySize * 0.091f,
        rotationAngle = 0f
    )

    init {
        closeButton = createCloseButtonModel()
    }

    fun init(renderProjection: Matrix4f) {
        val texturePath = this.javaClass.getResource("/textures/gui/InventoryGuiContainer.png")!!.path

        graphicalComponent = Model(
            texture = Texture2D.createInstance(texturePath)
        ).apply {
            shader = ShaderController.createTexturedShader(renderProjection)
            zLevel = 1f
            isPartOfWorldTranslation = false
        }

        addComponent(graphicalComponent!!, parameters)
        addComponent(closeButton, parameters)
    }

    fun updatePosition(pos: Point2D) {
        parameters.x = pos.x
        parameters.y = pos.y
    }

    private fun createCloseButtonModel(): GenericButton {
        val texturePath = this.javaClass.getResource("/textures/gui/chest/ButtonClose.png")!!.path

        return GenericButton(
            onClick = { println("onClick close!") },
            texturePath = texturePath,
            parameters = closeButtonParams
        )
    }
}