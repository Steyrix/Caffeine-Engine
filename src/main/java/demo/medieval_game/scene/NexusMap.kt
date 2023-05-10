package demo.medieval_game.scene

import demo.medieval_game.MedievalGameMatrixState
import demo.medieval_game.data.MapSceneInitializer
import demo.medieval_game.data.characterParameters
import demo.medieval_game.data.gameobject.Character
import demo.medieval_game.data.gameobject.TempSpritesHolder
import demo.medieval_game.data.starting_level.StartMapInitializer
import demo.medieval_game.data.starting_level.getNexusMapPreset
import engine.core.controllable.Direction
import engine.core.loop.AccumulatedTimeEvent
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.core.update.getCenterPoint
import engine.core.window.Window
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.tiled.scene.TileMapObject
import engine.feature.tiled.scene.TileMapScene
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*

class NexusMap(
        override val screenWidth: Float,
        override val screenHeight: Float,
        projection: Matrix4f? = null,
        switchTrigger: () -> Unit = {}
) : TileMapScene(projection) {

    private val tiledCollisionContext = TiledCollisionContext()

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    private var bbCollisionContext: BoundingBoxCollisionContext? = null
    private var boxInteractionContext: BoxInteractionContext? = null

    private var rounds = 0

    override fun init(session: Session, intent: SceneIntent?) {
        if (session !is MedievalGameSession) return

        super.init(session, intent)

        bbCollisionContext = session.bbCollisionContext
        boxInteractionContext = session.boxInteractionContext

        val tempSpritesHolder = gameContext.find { it is TempSpritesHolder } as? TempSpritesHolder
        val character = gameContext.find { it is Character } as Character

        // TODO: temp
        tiledMap?.let {
            val objects = StartMapInitializer.initAll(
                    renderProjection!!,
                    bbCollisionContext!!,
                    boxInteractionContext!!,
                    tempSpritesHolder!!
            ) { params -> it.createTraverser(params) }

            gameContext.addAll(objects)
            character.updateCollisionContext(tiledCollisionContext)
            character.updateBoundingBox()
        }

        intent?.let {
            determineDirection(it as MedievalGameSceneIntent)
        }
    }

    override fun initTileMap(projection: Matrix4f, screenWidth: Float, screenHeight: Float): TileMapObject {
        return MapSceneInitializer.initTileMapObject(
                getNexusMapPreset(),
                projection,
                listOf(tiledCollisionContext)
        )
    }

    override fun input(window: Window) {
        gameContext.forEach { it.input(window) }
    }

    override fun update(deltaTime: Float) {
        if (rounds < 2) {
            rounds++
            if (rounds == 2) {
                MedievalGameMatrixState.tempTranslation.x = screenWidth / 2 - characterParameters.xSize / 2
                MedievalGameMatrixState.tempTranslation.y = screenHeight / 2 - characterParameters.ySize / 2
            }
        }

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
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0f, 0.5f, 0f, 0.5f)

        gameContext.forEach { it.draw() }
    }

    override fun onSwitch(): MedievalGameSceneIntent {
        TODO("Not yet implemented")
    }

    private fun determineDirection(intent: MedievalGameSceneIntent) {
        val worldWidth = tiledMap?.worldSize?.x ?: 0f
        val worldHeight = tiledMap?.worldSize?.y ?: 0f
        var xMod = 0f
        var yMod = 0f

        when(intent.direction) {
            Direction.RIGHT -> {
                characterParameters.x = 0f
                xMod = 1f
            }
            Direction.LEFT -> {
                characterParameters.x = worldWidth - characterParameters.xSize
                xMod = 1f
            }
            Direction.UP -> {
                characterParameters.y = worldHeight - characterParameters.ySize
                yMod = 1f
            }
            Direction.DOWN -> {
                characterParameters.y = 0f
                yMod = 1f
            }
        }

        // TODO move out and fix
        MedievalGameMatrixState.worldTranslation.x *= yMod
        MedievalGameMatrixState.worldTranslation.y *= xMod
        MedievalGameMatrixState.tempTranslation.x *= yMod
        MedievalGameMatrixState.tempTranslation.y *= xMod

        val centerPoint = characterParameters.getCenterPoint()
        val horizontalTranslation = screenWidth - centerPoint.x
        val verticalTranslation = screenHeight - centerPoint.y

        MedievalGameMatrixState.translateWorld(
                horizontalTranslation * xMod,
                verticalTranslation * yMod
        )

        MedievalGameMatrixState.tempTranslation.x = screenWidth / 2 - characterParameters.xSize / 2
        MedievalGameMatrixState.tempTranslation.y = screenHeight / 2 - characterParameters.ySize / 2

        println(MedievalGameMatrixState.tempTranslation.x)
        println(MedievalGameMatrixState.tempTranslation.y)
    }
}