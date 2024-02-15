package demo.medieval_game.scene

import demo.medieval_game.data.MapSceneInitializer
import demo.medieval_game.data.gameobject.PlayableCharacter
import demo.medieval_game.data.gameobject.gui.chest.ChestGuiContainer
import demo.medieval_game.data.static_parameters.characterParameters
import demo.medieval_game.interaction.event.Loot
import demo.medieval_game.matrix.MedievalGameMatrixState
import engine.core.controllable.Direction
import engine.core.loop.AccumulatedTimeEvent
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.core.update.ParametersFactory
import engine.core.window.Window
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.interaction.broadcast.EventReceiver
import engine.feature.interaction.broadcast.InteractionEvent
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
) : TileMapScene(projection), EventReceiver {

    protected var character: PlayableCharacter? = null

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    protected var bbCollisionContext: BoundingBoxCollisionContext? = null
    protected var boxInteractionContext: BoxInteractionContext =
        BoxInteractionContext().also {
            it.listeners.add(this)
        }

    private var updateRounds = 0

    private var isHorizontalMapTransaction = false

    protected var textRenderer: TextRenderer? = null

    protected var chestGui: ChestGuiContainer = ChestGuiContainer(ParametersFactory.createEmptyStatic())
    private var showChestGui = false

    override fun init(session: Session, intent: SceneIntent?) {
        if (session !is MedievalGameSession) return

        super.init(session, intent)

        bbCollisionContext = BoundingBoxCollisionContext()

        character = session.sessionCharacter
        bbCollisionContext?.let {
            character?.addBoundingBoxCollider(it)
        }

        character?.addToInteractionContext(boxInteractionContext)

        intent?.let {
            handleMapTransaction(it as MedievalGameSceneIntent)
        }

        chestGui.init(renderProjection)
    }

    override fun initTileMap(projection: Matrix4f, screenWidth: Float, screenHeight: Float): TileMapEntity {
        val collisionContexts = tiledCollisionContext?.let {
            listOf(it)
        } ?: emptyList()

        return MapSceneInitializer.initTileMapObject(
            preset,
            projection,
            collisionContexts
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

        tiledCollisionContext?.update()
        bbCollisionContext?.update()
        boxInteractionContext?.update()
        chestGui.update(deltaTime)
    }

    private fun renderSetup() {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        glEnable(GL_STENCIL_TEST)
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE)

        glClearColor(0f, 0.5f, 0f, 0.5f)
        glClear(GL_COLOR_BUFFER_BIT)
        glClear(GL_STENCIL_BUFFER_BIT)
        glStencilMask(0x00)
    }

    override fun render(window: Window) {
        renderSetup()
        context.entitiesSortedByLevelZ().forEach {
            it.draw()
        }
        if (showChestGui) {
            chestGui.draw()
        }
    }

    override fun onSwitch() {
        tiledCollisionContext?.dispose()
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

    override fun proccessEvent(event: InteractionEvent) {
        when(event) {
            is Loot -> {
                chestGui.parameters.apply {
                    x = characterParameters.x
                    y = characterParameters.y
                    xSize = 400f
                    ySize = 496f
                }
                showChestGui = true
            }
        }
    }
}