package demo.labyrinth

import demo.labyrinth.data.*
import demo.labyrinth.data.gameobject.*
import engine.core.loop.AccumulatedTimeEvent
import engine.core.scene.GameObject
import engine.core.scene.Scene
import engine.core.scene.context.Bundle
import engine.core.window.Window
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*

class LabyrinthDemo(
        override val screenWidth: Float,
        override val screenHeight: Float
) : Scene {

    private val bbCollisionContext = BoundingBoxCollisionContext()
    private val tiledCollisionContext = TiledCollisionContext()
    private val boxInteractionContext = BoxInteractionContext()

    // todo: use game context, adapt to bundle
    private var objects = mutableListOf<GameObject>()

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    override var renderProjection: Matrix4f? = null

    override fun init(bundle: Bundle?) {
        renderProjection = Matrix4f().ortho(
                0f,
                screenWidth,
                screenHeight,
                0f,
                0f,
                1f
        )

        objects = LabyrinthInitializer.initAll(
                renderProjection!!,
                screenWidth,
                screenHeight,
                bbCollisionContext,
                tiledCollisionContext,
                boxInteractionContext
        )
    }

    override fun input(window: Window) {
        objects.forEach { it.input(window) }
    }

    override fun update(deltaTime: Float) {
        // todo encapsulate
        objects.forEach { entity ->
            entity.update(deltaTime)
            if (entity.isDisposed()) {
                actions.add(
                        AccumulatedTimeEvent(
                                timeLimit = 10f,
                                action = { objects.remove(entity) },
                                initialTime = 0f
                        )
                )
            }
        }

        actions.forEach { it.schedule(deltaTime) }

        setupDrawOrder()
        bbCollisionContext.update()
        tiledCollisionContext.update()
        boxInteractionContext.update()
    }

    override fun render(window: Window) {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0f, 0.5f, 0f, 0.5f)

        objects.forEach {
            it.draw()
        }
    }

    override fun onSwitch() {

    }

    override fun getBundle(): Bundle? {
        return null
    }

    private fun setupDrawOrder() {
        objects.sortBy {
            it.getZLevel()
        }
    }
}