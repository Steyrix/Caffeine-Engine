package engine.feature.tiled.scene

import engine.core.game_object.GameEntity
import engine.core.game_object.SpawnOptions
import engine.core.scene.GameContext
import engine.core.scene.Scene
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.tiled.data.lighting.LightMap
import engine.feature.tiled.data.lighting.LightSource
import org.joml.Matrix4f

abstract class TileMapScene(
    projection: Matrix4f
) : Scene {

    companion object {
        private const val LIGHTMAP_PRECISION = 50f
    }

    override val renderProjection = projection

    override val context: GameContext = GameContext.getInstance()

    protected var tiledCollisionContext: TiledCollisionContext? = null
    protected var tiledMap: TileMapEntity? = null

    private val lightSources: MutableList<LightSource> = mutableListOf()

    protected var lightMap: LightMap? = null

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
                precision = LIGHTMAP_PRECISION,
                projection = renderProjection,
                parameters = SetOfStatic2DParameters(
                    x = 0f,
                    y = 0f,
                    xSize = screenWidth * (screenWidth / LIGHTMAP_PRECISION),
                    ySize = screenHeight * (screenHeight / LIGHTMAP_PRECISION),
                    rotationAngle = 0f
                ),
                tileMap = it,
                lightSources = litLightSources,
                screenSizeX = screenWidth,
                screenSizeY = screenHeight
            )
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
}