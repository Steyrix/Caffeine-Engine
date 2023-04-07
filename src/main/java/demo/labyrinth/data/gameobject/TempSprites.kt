package demo.labyrinth.data.gameobject

import demo.labyrinth.ShaderController
import demo.labyrinth.data.hitAnimation
import engine.core.entity.CompositeEntity
import engine.core.loop.AccumulatedTimeEvent
import engine.core.render.render2D.AnimatedObject2D
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

object TempSprites : GameObject {

    override var it: CompositeEntity? = CompositeEntity()

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    private var renderProjection: Matrix4f? = null

    fun init(projection: Matrix4f) {
        renderProjection = projection
    }

    fun generateHit(posX: Float, posY: Float) {
        if (renderProjection == null) return

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
            x = posX
            y = posY
            xSize = 10f
            ySize = 10f
            shader = ShaderController.createAnimationShader(renderProjection!!)
        }

        val params = SetOfStatic2DParameters(
                x = posX,
                y = posY,
                xSize = 64f,
                ySize = 64f,
                rotationAngle = 0f
        )

        it?.addComponent(graphicalComponent, params)
        actions.add(
                AccumulatedTimeEvent(
                        timeLimit = 0.5f,
                        action = { _ ->
                            it?.removeComponent(params)
                        },
                        initialTime = 0f
                )
        )
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        actions.forEach {
            it.schedule(deltaTime)
        }
    }


}