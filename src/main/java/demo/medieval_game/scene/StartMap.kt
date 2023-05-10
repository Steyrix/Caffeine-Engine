package demo.medieval_game.scene

import demo.medieval_game.data.MapSceneInitializer
import demo.medieval_game.data.characterParameters
import demo.medieval_game.data.gameobject.*
import demo.medieval_game.data.starting_level.StartMapInitializer
import demo.medieval_game.data.starting_level.getStartingMapPreset
import engine.core.loop.AccumulatedTimeEvent
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.core.window.Window
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.geometry.Point2D
import engine.feature.interaction.BoxInteractionContext
import engine.feature.tiled.scene.TileMapObject
import engine.feature.tiled.scene.TileMapScene
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*

class StartMap(
        override val screenWidth: Float,
        override val screenHeight: Float,
        projection: Matrix4f? = null,
        private val switchTrigger: () -> Unit = {}
) : TileMapScene(projection) {

    private val tiledCollisionContext = TiledCollisionContext()

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    private var bbCollisionContext: BoundingBoxCollisionContext? = null
    private var boxInteractionContext: BoxInteractionContext? = null

    private var character: Character? = null

    override fun init(session: Session, intent: SceneIntent?) {
        if (session !is MedievalGameSession) return

        bbCollisionContext = session.bbCollisionContext
        boxInteractionContext = session.boxInteractionContext

        super.init(session, intent)

        val tempSpritesHolder = gameContext.find { it is TempSpritesHolder } as? TempSpritesHolder
        character = gameContext.find { it is Character } as Character

        tiledMap?.let {
            val objects = StartMapInitializer.initAll(
                    renderProjection!!,
                    bbCollisionContext!!,
                    boxInteractionContext!!,
                    tempSpritesHolder!!
            ) { params -> it.createTraverser(params) }

            gameContext.addAll(objects)
            character?.updateCollisionContext(tiledCollisionContext)
            character?.updateBoundingBox()
        }
    }

    override fun initTileMap(projection: Matrix4f, screenWidth: Float, screenHeight: Float): TileMapObject {
        return MapSceneInitializer.initTileMapObject(
                getStartingMapPreset(screenWidth, screenHeight),
                projection,
                listOf(tiledCollisionContext)
        )
    }

    override fun input(window: Window) {
        gameContext.forEach { it.input(window) }
    }

    // TODO: refactor
    override fun update(deltaTime: Float) {
        gameContext.forEach { entity ->
            entity.update(deltaTime)
            if (entity.isDisposed()) {
                actions.add(
                        AccumulatedTimeEvent(
                                timeLimit = 10f,
                                action = { gameContext.remove(entity) },
                                initialTime = 0f
                        )
                )
            }
        }

        actions.forEach { it.schedule(deltaTime) }

        super.update(deltaTime)
        tiledCollisionContext.update()
        bbCollisionContext?.update()
        boxInteractionContext?.update()

        if (character?.isOutOfMap() == true) {
            switchTrigger.invoke()
        }
    }

    override fun render(window: Window) {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0f, 0.5f, 0f, 0.5f)

        gameContext.forEach { it.draw() }
    }

    override fun onSwitch(): MedievalGameSceneIntent {
        return MedievalGameSceneIntent(
                direction = character!!.getDirection(),
                previousScenePos = Point2D(characterParameters.x, characterParameters.y)
        )
    }
}