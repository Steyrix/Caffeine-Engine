package demo.medieval_game.data.gameobject.gui.button

import demo.medieval_game.ShaderController
import demo.medieval_game.scene.MedievalGame
import engine.core.controllable.Controllable
import engine.core.entity.CompositeEntity
import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters

class GenericButton(
    var onClick: (Any?) -> Unit,
    texturePath: String,
    private val parameters: SetOfStatic2DParameters
) : CompositeEntity(), Controllable {

    companion object {
        private const val shaderUniformName = "isPressed"
    }

    private var isPressed = false

    private var model: Model? = null

    private val onHover = { value: Boolean ->
        isPressed = value
    }

    private val controller = ButtonController(
        parameters,
        onHover = onHover,
        onClick = onClick
    )

    init {
        val texture = Texture2D.createInstance(texturePath)

        model = Model(texture).apply {
            shader = ShaderController.createGenericButtonShader(MedievalGame.renderProjection)
            zLevel = 1f
            isPartOfWorldTranslation = false
        }

        addComponent(model!!, parameters)
        addComponent(controller, parameters)
    }

    override fun update(deltaTime: Float) {
        model?.shader?.let {
            it.bind()
            it.setUniform(shaderUniformName, isPressed)
        }
        super.update(deltaTime)
    }

    fun updatePosition(pos: Point2D) {
        parameters.x = pos.x
        parameters.y = pos.y
        controller.parameters.x = parameters.x
        controller.parameters.y = parameters.y
    }

    fun updateOnClick(func: (Any?) -> Unit) {
        controller.onClick = func
    }
}