package demo.medieval_game.data.gameobject.gui.button

import demo.medieval_game.ShaderController
import demo.medieval_game.scene.MedievalGame
import engine.core.entity.CompositeEntity
import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters

class GenericButton(
    onClick: (Any?) -> Unit,
    texturePath: String,
    private val parameters: SetOfStatic2DParameters
) : CompositeEntity() {

    companion object {
        private const val shaderUniformName = "isPressed"
    }

    private var isPressed = false

    private var model: Model? = null

    private val controller = ButtonController { arg ->
        onClick.invoke(arg)
        isPressed = !isPressed
    }

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
    }
}