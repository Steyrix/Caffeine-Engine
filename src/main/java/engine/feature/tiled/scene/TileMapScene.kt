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
import engine.feature.tiled.data.lighting.LightMap
import engine.feature.tiled.data.lighting.LightSource
import org.joml.Matrix4f
import org.joml.Vector2f

abstract class TileMapScene(
    projection: Matrix4f
) : Scene {

    protected var lightmapPrecision = 10f

    override val renderProjection = projection

    override val context: GameContext = GameContext.getInstance()

    protected var tiledCollisionContext: TiledCollisionContext? = null
    protected var tiledMap: TileMapEntity? = null

    private val lightSources: MutableList<LightSource> = mutableListOf()

    protected var lightMap: LightMap? = null
    protected var lightMapShader: Shader? = null

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

        tiledMap?.let {
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

        if (lightSources.isNotEmpty()) {
            lightMap = generateLightMap(renderProjection, lightSources)
        }
    }

    private fun generateLightMap(
        renderProjection: Matrix4f,
        lightSources: MutableList<LightSource>
    ): LightMap {
        tiledMap?.mapComponent?.let {

            val litLightSources = mutableListOf<LightSource>()
            litLightSources.addAll(lightSources.filter { src -> src.isLit })

            return LightMap(
                precision = lightmapPrecision,
                projection = renderProjection,
                parameters = SetOfStatic2DParameters(
                    x = 0f,
                    y = 0f,
                    xSize = screenWidth * (screenWidth / lightmapPrecision),
                    ySize = screenHeight * (screenHeight / lightmapPrecision),
                    rotationAngle = 0f
                ),
                tileMap = it,
                lightSources = litLightSources,
                screenSizeX = screenWidth,
                screenSizeY = screenHeight,
                translation = matrixState?.worldTranslation ?: Vector2f(0f, 0f)

            ).apply {
                lightMapShader?.let { shader ->
                    this.setShader(shader)
                }
            }
        } ?: throw IllegalStateException()
    }

    abstract fun initTileMap(
        projection: Matrix4f,
        screenWidth: Float,
        screenHeight: Float
    ): TileMapEntity

    fun spawn(entity: GameEntity, spawnOptions: SpawnOptions) {}

    fun addLightSource(lightSource: LightSource) {
        lightSources.add(lightSource)
        if (lightSource.isLit) {
            lightMap = generateLightMap(renderProjection, lightSources)
        }
    }

    fun removeLightSource(lightSource: LightSource) {
        lightSources.remove(lightSource)
        if (lightSource.isLit) {
            lightMap = generateLightMap(renderProjection, lightSources)
        }
    }

    fun setLightSourceLit(lightSource: LightSource, value: Boolean) {
        if (value != lightSource.isLit) {
            lightSource.isLit = value
            lightMap = generateLightMap(renderProjection, lightSources)
        }
    }

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

    fun updateLightMapShader(shader: Shader) {
        lightMap?.setShader(shader)
        lightMapShader = shader
    }
}