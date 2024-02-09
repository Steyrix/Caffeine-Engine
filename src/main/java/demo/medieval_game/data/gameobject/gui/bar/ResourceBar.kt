package demo.medieval_game.data.gameobject.gui.bar

import demo.medieval_game.ShaderController
import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

open class ResourceBar(
    private val objParams: SetOfParameters,
    private val barParams: SetOfStatic2DParameters,
    private val projection: Matrix4f,
    protected val onFilledChange: (Float) -> Unit = {},
    protected val isBoundToParams: Boolean = true,
    texturePath: String
) : CompositeEntity() {

    companion object {
        const val SHADER_UNIFORM_NAME = "filled"
    }

    private val graphicalComponent: Model

    protected var filled: Float = 1.0f

    init {
        graphicalComponent = Model(
            texture = Texture2D.createInstance(texturePath),
        ).apply {
            shader = ShaderController.createHpBarShader(projection)
        }

        addComponent(graphicalComponent, barParams)
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (isBoundToParams) {
            with(objParams) {
                barParams.x = x - ((barParams.xSize - xSize) / 2)
                barParams.y = y - 2 - barParams.ySize
            }
        }

        graphicalComponent.shader?.let {
            it.bind()
            it.setUniform(SHADER_UNIFORM_NAME, filled)
        }
    }
}