package demo.medieval_game.scene

import demo.medieval_game.data.gameobject.*
import demo.medieval_game.data.starting_level.LabyrinthInitializer
import engine.core.loop.AccumulatedTimeEvent
import engine.core.scene.GameObject
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
        override val screenHeight: Float,
        projection: Matrix4f? = null,
        private val boxInteractionContext: BoxInteractionContext,
        private val bbCollisionContext: BoundingBoxCollisionContext
) : TileMapScene(projection) {

    override var renderProjection: Matrix4f? = null

    override val gameContext: MutableList<GameObject> = mutableListOf()

    private val tiledCollisionContext = TiledCollisionContext()

    private var objects = mutableListOf<GameObject>()

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    override fun init(persistentObject: List<GameObject>) {
        super.init(persistentObject)

        val tempSpritesHolder = gameContext.find { it is TempSpritesHolder } as? TempSpritesHolder
        val character = gameContext.find { it is Character } as Character

        tiledMap?.let {
            val objects = LabyrinthInitializer.initAll(
                    renderProjection!!,
                    bbCollisionContext,
                    boxInteractionContext,
                    tempSpritesHolder!!
            ) { params -> it.createTraverser(params) }

            gameContext.addAll(objects)
            character.updateCollisionContext(tiledCollisionContext)
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
        gameContext.forEach { it.input(window) }
    }

    override fun update(deltaTime: Float) {
        gameContext.forEach { entity ->
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
        tiledCollisionContext.update()
        bbCollisionContext.update()
        boxInteractionContext.update()
    }

    override fun render(window: Window) {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0f, 0.5f, 0f, 0.5f)

        gameContext.forEach { it.draw() }
    }

    override fun onSwitch() {

    }

    private fun setupDrawOrder() {
        gameContext.sortBy {
            it.getZLevel()
        }
    }
}