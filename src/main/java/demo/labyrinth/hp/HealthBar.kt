package demo.labyrinth.hp

import demo.labyrinth.ShaderController
import demo.labyrinth.interaction.AttackInteraction
import engine.core.entity.CompositeEntity
import engine.core.render.render2D.OpenGlObject2D
import engine.core.texture.Texture2D
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import engine.feature.interaction.Interaction
import org.joml.Matrix4f

class HealthBar(
        private val objParams: SetOfParameters,
        private val barParams: SetOfStatic2DParameters,
        private val projection: Matrix4f
) : CompositeEntity() {

    companion object {
        private const val SHADER_UNIFORM_NAME = "filled"
    }

    private val graphicalComponent: OpenGlObject2D
    var filled: Float = 1.0f

    init {
        val textureFilePath = this.javaClass.getResource("/textures/healthbar_atlas.png")!!.path

        graphicalComponent = OpenGlObject2D(
                texture2D = Texture2D.createInstance(textureFilePath),
        ).apply {
            shader = ShaderController.createHpBarShader(projection)
        }

        addComponent(graphicalComponent, barParams)
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        with(objParams) {
            barParams.x = this.x - ((barParams.xSize - this.xSize) / 2)
            barParams.y = this.y - 2 - barParams.ySize
        }

        graphicalComponent.shader?.let {
            it.bind()
            it.setUniform(SHADER_UNIFORM_NAME, filled)
        }
    }

    override fun consumeInteraction(interaction: Interaction) {
        when(interaction) {
            is AttackInteraction -> filled -= interaction.damage
        }
    }
}