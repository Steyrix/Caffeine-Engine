package demo.medieval_game.data.gameobject

import demo.medieval_game.ShaderController
import demo.medieval_game.data.hitAnimation
import engine.core.entity.CompositeEntity
import engine.core.loop.AccumulatedTimeEvent
import engine.core.loop.SingleTimeEvent
import engine.core.render.primitive.Rectangle
import engine.core.render.render2D.AnimatedObject2D
import engine.core.scene.game_object.CompositeGameObject
import engine.core.scene.game_object.GameObject
import engine.core.scene.game_object.SingleGameObject
import engine.core.shader.Shader
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class TempSpritesHolder : CompositeGameObject() {

    private val objectToAction: MutableMap<GameObject, (Float) -> Unit> = mutableMapOf()
    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    private var renderProjection: Matrix4f? = null

    private var z: Float = Float.MAX_VALUE

    private var fadingAlpha = 0f
    private var defadingAlpha = 1f

    private val texture: Texture2D

    init {
        val texturePath = this.javaClass.getResource("/textures/blood_hit.png")!!.path
        texture = Texture2D.createInstance(texturePath)
    }

    fun init(projection: Matrix4f) {
        renderProjection = projection
    }

    fun generateHit(posX: Float, posY: Float) {
        if (renderProjection == null) return

        val obj = object : SingleGameObject() {
            override fun getZLevel() = posY + 0.5f
        }.apply {
            it = CompositeEntity()
        }
        addComponent(obj)

        val frameSizeX = 0.25f
        val frameSizeY = 0.25f

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

        obj.it?.addComponent(graphicalComponent, params)

        actions.add(
                SingleTimeEvent(
                        timeLimit = 0.8f,
                        action = {
                            println("removing hit")
                            removeComponent(obj)
                        },
                        initialTime = 0f
                )
        )

        println("generate hit ${getZLevel()} ${isDisposed()} ${actions.size}")
    }

    fun startScreenFading(
            worldWidth: Float,
            worldHeight: Float,
            onFinish: () -> Unit
    ) {
        if (renderProjection == null) return
        println("fading $fadingAlpha")
        val obj = object : SingleGameObject() {
            override fun getZLevel() = Float.MAX_VALUE
        }.apply {
            it = CompositeEntity()
        }
        addComponent(obj)

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

        obj.it?.addComponent(graphicalComponent, params)

        actions.add(
                SingleTimeEvent(
                        timeLimit = 2f,
                        action = {
                            removeComponent(obj)
                            objectToAction.remove(obj)
                            fadingAlpha = 0f
                            onFinish.invoke()
                        },
                        initialTime = 0f
                )
        )

        objectToAction[obj] = { deltaTime ->
            fadingAlpha += deltaTime * 0.5f
            graphicalComponent.shader?.let {
                it.bind()
                it.setUniform(Shader.ALPHA, fadingAlpha)
            }
        }
    }

    fun startScreenDefading(
            worldWidth: Float,
            worldHeight: Float,
    ) {
        if (renderProjection == null) return
        println("defading $defadingAlpha")
        val obj = object : SingleGameObject() {
            override fun getZLevel() = Float.MAX_VALUE
        }.apply {
            it = CompositeEntity()
        }
        addComponent(obj)

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

        obj.it?.addComponent(graphicalComponent, params)

        actions.add(
                SingleTimeEvent(
                        timeLimit = 2f,
                        action = {
                            removeComponent(obj)
                            objectToAction.remove(obj)
                            defadingAlpha = 1f
                        },
                        initialTime = 0f
                )
        )

        objectToAction[obj] = { deltaTime ->
            defadingAlpha -= deltaTime * 0.5f
            graphicalComponent.shader?.let {
                it.bind()
                it.setUniform(Shader.ALPHA, defadingAlpha)
            }
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        actions.removeAll { it is SingleTimeEvent && it.isFinished }
        actions.forEach {
            it.schedule(deltaTime)
        }

        objectToAction.values.forEach {
            it.invoke(deltaTime)
        }
    }
}