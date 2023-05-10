package demo.medieval_game.scene

import demo.medieval_game.data.MapSceneInitializer
import demo.medieval_game.matrix.MedievalGameMatrixState
import engine.core.controllable.Direction
import engine.core.loop.AccumulatedTimeEvent
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.core.window.Window
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.tiled.scene.TileMapObject
import engine.feature.tiled.scene.TileMapPreset
import engine.feature.tiled.scene.TileMapScene
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C

abstract class MedievalGameScene(
        private val preset: TileMapPreset,
        override val screenWidth: Float,
        override val screenHeight: Float,
        projection: Matrix4f? = null,
) : TileMapScene(projection) {

    protected val tiledCollisionContext = TiledCollisionContext()

    protected val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    protected var bbCollisionContext: BoundingBoxCollisionContext? = null
    protected var boxInteractionContext: BoxInteractionContext? = null

    private var rounds = 0
    private var isHorizontalMapTransaction = false

    override fun init(session: Session, intent: SceneIntent?) {
        if (session !is MedievalGameSession) return

        super.init(session, intent)

        bbCollisionContext = session.bbCollisionContext
        boxInteractionContext = session.boxInteractionContext

        intent?.let {
            handleMapTransaction(it as MedievalGameSceneIntent)
        }
    }

    override fun initTileMap(projection: Matrix4f, screenWidth: Float, screenHeight: Float): TileMapObject {
        return MapSceneInitializer.initTileMapObject(
                preset,
                projection,
                listOf(tiledCollisionContext)
        )
    }

    override fun input(window: Window) {
        gameContext.forEach { it.input(window) }
    }

    override fun update(deltaTime: Float) {
        postMapTransactionAction()

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
    }

    override fun render(window: Window) {
        GL33C.glEnable(GL33C.GL_BLEND)
        GL33C.glBlendFunc(GL33C.GL_SRC_ALPHA, GL33C.GL_ONE_MINUS_SRC_ALPHA)
        GL33C.glClear(GL33C.GL_COLOR_BUFFER_BIT)
        GL33C.glClearColor(0f, 0.5f, 0f, 0.5f)

        gameContext.forEach { it.draw() }
    }

    protected fun handleMapTransaction(intent: MedievalGameSceneIntent) {
        isHorizontalMapTransaction =
                intent.direction == Direction.RIGHT
                        || intent.direction == Direction.LEFT

        MedievalGameMatrixState.handleMapTransaction(
                intent.direction,
                screenWidth,
                screenHeight,
                worldWidth = tiledMap?.worldSize?.x ?: 0f,
                worldHeight = tiledMap?.worldSize?.y ?: 0f
        )
    }

    protected fun postMapTransactionAction() {
        if (rounds < 2) {
            rounds++
            if (rounds == 2) {
                MedievalGameMatrixState.postMapTransactionAction(
                        isHorizontalMapTransaction,
                        screenWidth,
                        screenHeight
                )
            }
        }
    }
}