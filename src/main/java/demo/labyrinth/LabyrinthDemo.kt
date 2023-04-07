package demo.labyrinth

import demo.labyrinth.data.*
import demo.labyrinth.data.gameobject.*
import engine.core.scene.Scene
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
                tiledCollisionContext,
                boxInteractionContext
        )
    }

    override fun input(window: Window) {
        Character.it?.input(window)
    }

    override fun update(deltaTime: Float) {
        Character.update(deltaTime)
        NPCs.update(deltaTime)
        bbCollisionContext.update()
        tiledCollisionContext.update()
        boxInteractionContext.update()
        TempSprites.update(deltaTime)

        Campfire.update(deltaTime)
        GameMap.update(deltaTime)
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0f, 0.5f, 0f, 0.5f)

        GameMap.draw()
        Campfire.draw()
        Character.draw()
        NPCs.draw()
        TempSprites.draw()
    }
}