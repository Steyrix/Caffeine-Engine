package demo.medieval_game.data.gameobject.sprite_holder

import demo.medieval_game.ShaderController
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.loop.AccumulatedTimeEvent
import engine.core.loop.SingleTimeEvent
import engine.core.render.primitive.Rectangle
import engine.core.game_object.SingleGameEntity
import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class SharedSpritesHolder : SingleGameEntity() {

    private var alpha = 0f
    private var isFadeIn = true
    private var isFinishedActionInvoked = false

    private var renderProjection: Matrix4f? = null

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()
    private val componentToAction: MutableMap<Entity, (Float) -> Unit> = mutableMapOf()

    fun init(projection: Matrix4f) {
        renderProjection = projection
        it = CompositeEntity()
    }

    fun startScreenFading(
        worldWidth: Float,
        worldHeight: Float,
        onFinish: () -> Unit
    ) {
        if (renderProjection == null) return

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
                timeLimit = 4f,
                action = { _ ->
                    it?.removeComponent(graphicalComponent)
                    componentToAction.remove(graphicalComponent)
                    alpha = 0f
                    isFadeIn = true
                },
                initialTime = 0f
            )
        )


        // TODO think of where to put invoke, stop updating scene on fading
        componentToAction[graphicalComponent] = { deltaTime ->
            if (alpha < 1.1f && isFadeIn) {
                alpha += deltaTime * 0.5f
            } else {
                isFadeIn = false
                alpha -= deltaTime * 0.5f
                if (!isFinishedActionInvoked) {
                    isFinishedActionInvoked = true
                    onFinish.invoke()
                }
            }
            graphicalComponent.shader?.let {
                it.bind()
                it.setUniform(Shader.ALPHA, alpha)
            }
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        actions.removeAll { it is SingleTimeEvent && it.isFinished }
        actions.forEach {
            it.schedule(deltaTime)
        }

        componentToAction.values.forEach {
            it.invoke(deltaTime)
        }
    }

    override fun getZLevel(): Float {
        return Float.POSITIVE_INFINITY
    }
}