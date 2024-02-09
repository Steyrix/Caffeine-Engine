package demo.medieval_game.data.gameobject.gui.hp

import demo.medieval_game.ShaderController
import demo.medieval_game.interaction.PlayerAttackInteraction
import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import engine.feature.interaction.Interaction
import org.joml.Matrix4f

class HealthBar(
    private val objParams: SetOfParameters,
    private val barParams: SetOfStatic2DParameters,
    private val projection: Matrix4f,
    private val onFilledChange: (Float) -> Unit = {}
) : CompositeEntity() {

    companion object {
        private const val SHADER_UNIFORM_NAME = "filled"
    }

    private val graphicalComponent: Model

    // TODO: allow subscription
    private var filled: Float = 1.0f

    init {
        val textureFilePath = this.javaClass.getResource("/textures/HealthBarAtlas.png")!!.path

        graphicalComponent = Model(
            texture = Texture2D.createInstance(textureFilePath),
        ).apply {
            shader = ShaderController.createHpBarShader(projection)
        }

        addComponent(graphicalComponent, barParams)
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        with(objParams) {
            barParams.x = x - ((barParams.xSize - xSize) / 2)
            barParams.y = y - 2 - barParams.ySize
        }

        graphicalComponent.shader?.let {
            it.bind()
            it.setUniform(SHADER_UNIFORM_NAME, filled)
        }
    }

    override fun consumeInteraction(interaction: Interaction) {
        when (interaction) {
            is PlayerAttackInteraction -> {
                filled -= interaction.damage
                onFilledChange(filled)
            }
        }
    }
}