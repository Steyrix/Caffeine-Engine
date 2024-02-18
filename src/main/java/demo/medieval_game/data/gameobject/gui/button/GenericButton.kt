package demo.medieval_game.data.gameobject.gui.button

import demo.medieval_game.ShaderController
import demo.medieval_game.scene.MedievalGame
import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.texture.Texture2D

class GenericButton(
    onClick: (Any?) -> Unit,
    texturePath: String
) : CompositeEntity() {

    companion object {
        private const val shaderUniformName = "isPressed"
    }

    private var isPressed = false

    private var model: Model? = null

    init {
        val texture = Texture2D.createInstance(texturePath)

        model = Model(texture).apply {
            shader = ShaderController.createGenericButtonShader(MedievalGame.renderProjection)
            zLevel = 1f
            isPartOfWorldTranslation = false
        }
    }

    override fun update(deltaTime: Float) {
        model?.shader?.let {
            it.bind()
            it.setUniform(shaderUniformName, isPressed)
        }
        super.update(deltaTime)
    }
}