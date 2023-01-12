package demo.labyrinth

import demo.labyrinth.data.*
import demo.labyrinth.data.Map
import engine.core.scene.Scene
import engine.core.window.Window
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*

class LabyrinthDemo(
        override val screenWidth: Float,
        override val screenHeight: Float
) : Scene {

    private val bbCollisionContext = BoundingBoxCollisionContext()
    private val tiledCollisionContext = TiledCollisionContext()

    override var renderProjection: Matrix4f? = null

    override fun init() {
        renderProjection = Matrix4f().ortho(
                0f,
                screenWidth,
                screenHeight,
                0f,
                0f,
                1f
        )

        LabyrinthInitializer.initAll(
                renderProjection!!,
                screenWidth,
                screenHeight,
                bbCollisionContext,
                tiledCollisionContext
        )
    }

    override fun input(window: Window) {
        Character.it?.input(window)
    }

    override fun update(deltaTime: Float) {
        Character.update(deltaTime)
        Skeletons.update(deltaTime)
        Crate.it?.update(deltaTime)
        bbCollisionContext.update()
        tiledCollisionContext.update()

        Campfire.update(deltaTime)
        Map.update(deltaTime)
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0f, 0.5f, 0f, 0.5f)

        Map.draw()
        Crate.draw()
        Campfire.draw()
        Character.draw()
        Skeletons.draw()
    }
}