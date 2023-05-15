package demo.medieval_game.data.gameobject

import demo.medieval_game.ShaderController
import demo.medieval_game.data.hitAnimation
import engine.core.entity.CompositeEntity
import engine.core.loop.AccumulatedTimeEvent
import engine.core.loop.SingleTimeEvent
import engine.core.render.primitive.Rectangle
import engine.core.render.render2D.AnimatedObject2D
import engine.core.scene.GameObject
import engine.core.shader.Shader
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class TempSpritesHolder : GameObject {

    override var it: CompositeEntity? = CompositeEntity()

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()
    private val updateActions: MutableList<(Float) -> Unit> = mutableListOf()

    private var renderProjection: Matrix4f? = null

    private var z: Float = -1f

    private var alpha = 0f

    fun init(projection: Matrix4f) {
        renderProjection = projection
    }

    fun generateHit(posX: Float, posY: Float) {
        if (renderProjection == null) return

        z = posY + 0.5f

        val frameSizeX = 0.25f
        val frameSizeY = 0.25f

        val texturePath = this.javaClass.getResource("/textures/blood_hit.png")!!.path
        val texture = Texture2D.createInstance(texturePath)

        val graphicalComponent = AnimatedObject2D(
            frameSizeX,
            frameSizeY,
            texture = texture,
            animations = hitAnimation
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection!!)
        }

        val params = SetOfStatic2DParameters(
                x = posX,
                y = posY,
                xSize = 128f,
                ySize = 128f,
                rotationAngle = 0f
        )

        it?.addComponent(graphicalComponent, params)
        actions.add(
                SingleTimeEvent(
                        timeLimit = 0.8f,
                        action = { _ ->
                            it?.removeComponent(graphicalComponent)
                        },
                        initialTime = 0f
                )
        )
    }

    fun startScreenFading(
            screenWidth: Float,
            screenHeight: Float,
            onFinish: () -> Unit
    ) {
        if (renderProjection == null) return

        val params = SetOfStatic2DParameters(
                x = 0f,
                y = 0f,
                xSize = screenWidth,
                ySize = screenHeight,
                rotationAngle = 0f
        )

        val graphicalComponent = Rectangle(0f, 0f, 0f).apply {
            shader = ShaderController.createPrimitiveShader(renderProjection!!)
        }

        it?.addComponent(graphicalComponent, params)

        actions.add(
                SingleTimeEvent(
                        timeLimit = 1.5f,
                        action = { _ ->
                            it?.removeComponent(graphicalComponent)
                            onFinish.invoke()
                        },
                        initialTime = 0f
                )
        )

        updateActions.add { deltaTime ->
            alpha += deltaTime * 0.5f
            graphicalComponent.shader?.let {
                it.bind()
                it.setUniform(Shader.ALPHA, alpha)
            }
        }

        z = Float.MAX_VALUE
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        actions.removeIf { it is SingleTimeEvent && it.isFinished }
        actions.forEach {
            it.schedule(deltaTime)
        }

        updateActions.forEach {
            it.invoke(deltaTime)
        }
    }

    override fun getZLevel(): Float {
        return z
    }
}