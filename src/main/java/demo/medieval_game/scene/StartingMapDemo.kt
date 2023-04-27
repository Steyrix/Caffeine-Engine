package demo.medieval_game.scene

import demo.medieval_game.data.*
import engine.core.loop.AccumulatedTimeEvent
import engine.core.scene.GameObject
import engine.core.update.SetOfParameters
import engine.core.window.Window
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.tiled.scene.TileMapObject
import engine.feature.tiled.scene.TileMapScene
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*

class StartingMapDemo(
        override val screenWidth: Float,
        override val screenHeight: Float
) : TileMapScene() {

    override var renderProjection: Matrix4f? = null

    override val gameContext: MutableMap<GameObject, SetOfParameters> = mutableMapOf()

    private val bbCollisionContext = BoundingBoxCollisionContext()
    private val tiledCollisionContext = TiledCollisionContext()
    private val boxInteractionContext = BoxInteractionContext()

    private var objects = mutableListOf<GameObject>()

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    override fun init() {
        super.init()

        renderProjection = Matrix4f().ortho(
                0f,
                screenWidth,
                screenHeight,
                0f,
                0f,
                1f
        )

        tiledMap?.let {
            objects = LabyrinthInitializer.initAll(
                    renderProjection!!,
                    screenWidth,
                    screenHeight,
                    bbCollisionContext,
                    tiledCollisionContext,
                    boxInteractionContext
            ) { params -> it.createTraverser(params) }
        }
    }

    override fun initTileMap(projection: Matrix4f, screenWidth: Float, screenHeight: Float): TileMapObject {
        return LabyrinthInitializer.initTileMapObject(
                projection,
                screenWidth,
                screenHeight,
                listOf(tiledCollisionContext)
        )
    }

    override fun input(window: Window) {
        objects.forEach { it.input(window) }
    }

    override fun update(deltaTime: Float) {
        gameContext.forEach { it.key.update(deltaTime) }

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

        gameContext.forEach { it.key.draw() }

        objects.forEach {
            it.draw()
        }
    }

    override fun onSwitch() {

    }

    private fun setupDrawOrder() {
        objects.sortBy {
            it.getZLevel()
        }
    }
}