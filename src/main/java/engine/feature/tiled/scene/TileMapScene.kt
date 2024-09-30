package engine.feature.tiled.scene

import engine.core.entity.CompositeEntity
import engine.core.game_object.GameEntity
import engine.core.game_object.SpawnOptions
import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.core.render.util.DefaultBufferData
import engine.core.scene.GameContext
import engine.core.scene.Scene
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.matrix.MatrixState
import engine.feature.tiled.data.TileMap
import org.joml.Matrix4f

@Suppress("MemberVisibilityCanBePrivate")
abstract class TileMapScene(
    projection: Matrix4f
) : Scene, LightMapHolder() {

    override val renderProjection = projection

    override val lightMapProjection = projection
    override var lightMapScreenWidth = 0f
    override var lightMapScreenHeight = 0f
    override var worldWidth = 0f
    override var worldHeight = 0f
    override var holderMap: TileMap? = null

    override var isDebugFlag: Boolean = false

    override val context: GameContext = GameContext.getInstance()

    protected var tiledCollisionContext: TiledCollisionContext? = null
    protected var tiledMap: PresettedTileMap? = null

    protected var tileHighlighting: CompositeEntity? = null
    protected val highlightParams = SetOfStatic2DParameters.createEmpty()
    private var highlightedTile: Int = -1
    protected var tileNetShader: Shader? = null

    private var matrixState: MatrixState? = null

    override fun init(
        session: Session,
        intent: SceneIntent?,
        isDebugFlag: Boolean
    ) {
        super.init(session, intent, isDebugFlag)

        tiledMap = initTileMap(
            renderProjection,
            screenWidth,
            screenHeight
        )

        lightMapScreenWidth = screenWidth
        lightMapScreenHeight = screenHeight
        worldWidth = tiledMap?.mapComponent?.getWorldWidth() ?: 0f
        worldHeight = tiledMap?.mapComponent?.getWorldHeight() ?: 0f

        tiledMap?.let {
            holderMap = it.mapComponent
            context.add(it)
            val objects = it.retrieveObjectEntities()
            context.addAll(objects)

            tiledCollisionContext = TiledCollisionContext(
                it.retrieveNonCollisionLayers(),
                it.retrieveObjectLayers(),
                it.mapComponent!!
            )
            it.addToCollisionContext(tiledCollisionContext!!)
            it.isDebugMeshEnabled = this.isDebugFlag
        }

        matrixState = session.matrixState
    }

    abstract fun initTileMap(
        projection: Matrix4f,
        screenWidth: Float,
        screenHeight: Float
    ): PresettedTileMap

    fun spawn(entity: GameEntity, spawnOptions: SpawnOptions) {}

    protected fun highlightTile(
        pos: Point2D,
        highlightingShader: Shader,
        selection: TileSelectionData
    ) {
        val map = tiledMap?.mapComponent ?: return
        val tileIndex = map.getTileIndex(pos.x, pos.y)
        val startPos = map.getTilePosition(tileIndex)

        highlightParams.apply {
            x = startPos.x
            y = startPos.y
            xSize = map.absoluteTileWidth() * selection.width
            ySize = map.absoluteTileHeight() * selection.height
            rotationAngle = 0f
        }

        if (tileIndex != highlightedTile) {
            tileHighlighting = CompositeEntity()

            tileHighlighting?.addComponent(createHighlightModel(highlightingShader), highlightParams)
            tileHighlighting?.addComponent(createTileNet(selection), highlightParams)

            selection.extraModel?.let {
                tileHighlighting?.addComponent(it, highlightParams)
                it.isPartOfWorldTranslation = false
                it.zLevel = 1f
            }

            highlightedTile = tileIndex
        }
    }

    private fun createHighlightModel(highlightingShader: Shader): Model {
        return Model(
            dataArrays = listOf(
                DefaultBufferData.RECTANGLE_INDICES,
                DefaultBufferData.getColorBuffer(0f, 1f, 0f)
            ),
            verticesCount = DefaultBufferData.RECTANGLE_INDICES.size / 2
        ).apply {
            shader = highlightingShader
            isPartOfWorldTranslation = false
            zLevel = 0f
        }
    }

    private fun createTileNet(selection: TileSelectionData): Model {
        tileNetShader?.let { shader ->
            return tiledMap?.mapComponent?.getNetForTileSelection(selection, shader) ?: throw Exception()
        } ?: throw Exception()
    }

    fun disableHighlighting() {
        highlightedTile = -1
        tileHighlighting = null
    }
}