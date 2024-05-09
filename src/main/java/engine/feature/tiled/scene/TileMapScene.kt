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

abstract class TileMapScene(
    projection: Matrix4f
) : Scene, LightMapHolder() {

    override val renderProjection = projection
    override val lightMapProjection = projection
    override var lightMapScreenWidth = 0f
    override var lightMapScreenHeight = 0f
    override var holderMap: TileMap? = null
    override var isDebugFlag: Boolean = false

    override val context: GameContext = GameContext.getInstance()

    protected var tiledCollisionContext: TiledCollisionContext? = null
    protected var tiledMap: TileMapEntity? = null

    protected var tileHighlighting: CompositeEntity? = null
    protected val highlightParams = SetOfStatic2DParameters.createEmpty()
    private var highlightedTile: Int = -1

    private var matrixState: MatrixState? = null

    override fun init(session: Session, intent: SceneIntent?) {
        super.init(session, intent)

        tiledMap = initTileMap(
            renderProjection,
            screenWidth,
            screenHeight
        )

        lightMapScreenWidth = screenWidth
        lightMapScreenHeight = screenHeight

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
        }

        matrixState = session.matrixState
    }

    abstract fun initTileMap(
        projection: Matrix4f,
        screenWidth: Float,
        screenHeight: Float
    ): TileMapEntity

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
            xSize = map.absoluteTileWidth * selection.width
            ySize = map.absoluteTileHeight * selection.height
        }

        if (tileIndex != highlightedTile) {
            tileHighlighting = CompositeEntity()

            val underlyingHighlight = Model(
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
            tileHighlighting?.addComponent(underlyingHighlight, highlightParams)
            selection.extraModel?.let {
                tileHighlighting?.addComponent(it, highlightParams)
                it.isPartOfWorldTranslation = false
                it.zLevel = 1f
            }

            highlightedTile = tileIndex
        }
    }

    fun disableHighlighting() {
        highlightedTile = -1
        tileHighlighting = null
    }
}