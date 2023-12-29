package demo.medieval_game.data.gameobject

import demo.medieval_game.ShaderController
import demo.medieval_game.data.hitAnimation
import engine.core.entity.CompositeEntity
import engine.core.loop.AccumulatedTimeEvent
import engine.core.loop.SingleTimeEvent
import engine.core.render.AnimatedModel2D
import engine.core.game_object.CompositeGameEntity
import engine.core.game_object.SingleGameEntity
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class TempSpritesHolder : CompositeGameEntity() {

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    private var renderProjection: Matrix4f? = null

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

        val frameSizeX = 0.25f
        val frameSizeY = 0.25f

        val graphicalComponent = AnimatedModel2D(
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

        val obj = object : SingleGameEntity() {
            override fun getZLevel() = posY + 0.5f
        }.apply {
            it = CompositeEntity().addComponent(graphicalComponent, params)
        }
        addComponent(obj)

        actions.add(
                SingleTimeEvent(
                        timeLimit = 0.8f,
                        action = {
                            removeComponent(obj)
                        },
                        initialTime = 0f
                )
        )
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        actions.removeAll { it is SingleTimeEvent && it.isFinished }
        actions.forEach {
            it.schedule(deltaTime)
        }
    }
}