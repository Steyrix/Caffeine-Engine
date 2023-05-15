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

    private var fadingAlpha = 0f
    private var defadingAlpha = 1f

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
            worldWidth: Float,
            worldHeight: Float,
            onFinish: () -> Unit
    ) {
        if (renderProjection == null) return
        println("fading $fadingAlpha")

        val params = SetOfStatic2DParameters(
                x = 0f,
                y = 0f,
                xSize = worldWidth,
                ySize = worldHeight,
                rotationAngle = 0f
        )

        val graphicalComponent = Rectangle(0f, 0f, 0f).apply {
            shader = ShaderController.createPrimitiveShader(renderProjection!!)
        }

        it?.addComponent(graphicalComponent, params)

        actions.add(
                SingleTimeEvent(
                        timeLimit = 2f,
                        action = { _ ->
                            it?.removeComponent(graphicalComponent)
                            fadingAlpha = 0f
                            onFinish.invoke()
                        },
                        initialTime = 0f
                )
        )

        updateActions.add { deltaTime ->
            fadingAlpha += deltaTime * 0.5f
            graphicalComponent.shader?.let {
                it.bind()
                it.setUniform(Shader.ALPHA, fadingAlpha)
            }
        }

        z = Float.MAX_VALUE
    }

    fun startScreenDefading(
            worldWidth: Float,
            worldHeight: Float,
    ) {
        if (renderProjection == null) return
        println("defading $defadingAlpha")

        val params = SetOfStatic2DParameters(
                x = 0f,
                y = 0f,
                xSize = worldWidth,
                ySize = worldHeight,
                rotationAngle = 0f
        )

        val graphicalComponent = Rectangle(0f, 0f, 0f).apply {
            shader = ShaderController.createPrimitiveShader(renderProjection!!)
        }

        it?.addComponent(graphicalComponent, params)

        actions.add(
                SingleTimeEvent(
                        timeLimit = 2f,
                        action = { _ ->
                            it?.removeComponent(graphicalComponent)
                            defadingAlpha = 1f
                        },
                        initialTime = 0f
                )
        )

        // should delete update Action
        updateActions.add { deltaTime ->
            defadingAlpha -= deltaTime * 0.5f
            graphicalComponent.shader?.let {
                it.bind()
                it.setUniform(Shader.ALPHA, defadingAlpha)
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