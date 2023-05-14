package demo.medieval_game.data.gameobject

import demo.medieval_game.ShaderController
import demo.medieval_game.data.hitAnimation
import engine.core.entity.CompositeEntity
import engine.core.loop.AccumulatedTimeEvent
import engine.core.render.primitive.Rectangle
import engine.core.render.render2D.AnimatedObject2D
import engine.core.scene.GameObject
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class TempSpritesHolder : GameObject {

    override var it: CompositeEntity? = CompositeEntity()

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    private var renderProjection: Matrix4f? = null

    private var z: Float = -1f

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
                AccumulatedTimeEvent(
                        timeLimit = 0.8f,
                        action = { _ ->
                            it?.removeComponent(graphicalComponent)
                        },
                        initialTime = 0f
                )
        )
    }

    // TODO: set up screen coords
    fun generateBlackScreen() {
        val params = SetOfStatic2DParameters(
                x = 0f,
                y = 0f,
                xSize = 300f,
                ySize = 300f,
                rotationAngle = 0f
        )

        val graphicalComponent = Rectangle(0f, 0f, 0f).apply {
            // todo add primitive shader
        }

        it?.addComponent(graphicalComponent, params)
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        actions.forEach {
            it.schedule(deltaTime)
        }
    }

    override fun getZLevel(): Float {
        return z
    }
}