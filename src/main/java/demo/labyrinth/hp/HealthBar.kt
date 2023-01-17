package demo.labyrinth.hp

import demo.labyrinth.ShaderController
import engine.core.entity.CompositeEntity
import engine.core.render.render2D.OpenGlObject2D
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f
import kotlin.random.Random

class HealthBar(
        private val characterParams: SetOf2DParametersWithVelocity,
        private val barParams: SetOfStatic2DParameters,
        private val projection: Matrix4f
) : CompositeEntity() {

    private val graphicalComponent: OpenGlObject2D

    init {
        val textureFilePath = this.javaClass.getResource("/textures/healthbar_atlas.png")!!.path

        graphicalComponent = OpenGlObject2D(
                texture2D = Texture2D.createInstance(textureFilePath),
        ).apply {
            shader = ShaderController.createHpBarShader(projection)
        }

        addComponent(graphicalComponent, barParams)
    }

    // TODO assign filled value from external environment
    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        with(characterParams) {
            barParams.x = this.x - ((barParams.xSize - this.xSize) / 2)
            barParams.y = this.y - 2 - barParams.ySize
        }

        graphicalComponent.shader?.let {
            it.bind()
            it.setUniform("filled", Random.nextFloat())
        }
    }
}