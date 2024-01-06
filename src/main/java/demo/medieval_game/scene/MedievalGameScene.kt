package demo.medieval_game.scene

import demo.medieval_game.data.MapSceneInitializer
import demo.medieval_game.data.gameobject.PlayableCharacter
import demo.medieval_game.matrix.MedievalGameMatrixState
import engine.core.controllable.Direction
import engine.core.loop.AccumulatedTimeEvent
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.core.window.Window
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.text.TextRenderer
import engine.feature.tiled.scene.TileMapEntity
import engine.feature.tiled.scene.TileMapPreset
import engine.feature.tiled.scene.TileMapScene
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*

abstract class MedievalGameScene(
    private val preset: TileMapPreset,
    override val screenWidth: Float,
    override val screenHeight: Float,
    projection: Matrix4f
) : TileMapScene(projection) {

    protected var character: PlayableCharacter? = null

    protected val tiledCollisionContext = TiledCollisionContext()

    protected val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    protected var bbCollisionContext: BoundingBoxCollisionContext? = null
    protected var boxInteractionContext: BoxInteractionContext? = null

    private var updateRounds = 0

    private var isHorizontalMapTransaction = false

    protected var textRenderer: TextRenderer? = null

    override fun init(session: Session, intent: SceneIntent?) {
        if (session !is MedievalGameSession) return

        super.init(session, intent)

        character = session.sessionCharacter

        bbCollisionContext = session.bbCollisionContext
        boxInteractionContext = session.boxInteractionContext

        intent?.let {
            handleMapTransaction(it as MedievalGameSceneIntent)
        }
    }

    override fun initTileMap(projection: Matrix4f, screenWidth: Float, screenHeight: Float): TileMapEntity {
        return MapSceneInitializer.initTileMapObject(
            preset,
            projection,
            listOf(tiledCollisionContext)
        )
    }

    override fun input(window: Window) {
        context.forEach { it.input(window) }
    }

    override fun update(deltaTime: Float) {
        postMapTransactionAction()

        // TODO: move out
        context.forEach { entity ->
            entity.update(deltaTime)
            if (entity.isDisposed()) {
                actions.add(
                    AccumulatedTimeEvent(
                        timeLimit = 10f,
                        action = { context.remove(entity) },
                        initialTime = 0f
                    )
                )
            }
        }

        actions.forEach { it.schedule(deltaTime) }

        tiledCollisionContext.update()
        bbCollisionContext?.update()
        boxInteractionContext?.update()
    }

    private inline fun renderSetup() {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0f, 0.5f, 0f, 0.5f)
    }

    override fun render(window: Window) {
        renderSetup()
        context.entitiesSortedByLevelZ().forEach {
            it.draw()
        }
    }

    override fun onSwitch() {
        tiledCollisionContext.dispose()
    }

    protected open fun handleMapTransaction(intent: MedievalGameSceneIntent) {
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

    protected open fun postMapTransactionAction() {
        if (updateRounds >= 2) return
        updateRounds++
        if (updateRounds == 2) {
            MedievalGameMatrixState.postMapTransactionAction(
                isHorizontalMapTransaction,
                screenWidth,
                screenHeight
            )
        }
    }
}