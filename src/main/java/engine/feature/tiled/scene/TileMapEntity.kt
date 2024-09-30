package engine.feature.tiled.scene

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.game_object.SingleGameEntity
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.CollisionContext
import engine.core.geometry.Point2D
import engine.core.loop.AccumulatedTimeEvent
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.procedural.generators.ProceduralGenerator
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.`object`.MapObjectEntity
import engine.feature.tiled.data.`object`.MapObjectRetriever
import org.joml.Matrix4f

open class TileMapEntity(
    private val mapPresets: TileMapPreset,
    private val isProcedural: Boolean = false,
    private val proceduralGenerator: ProceduralGenerator? = null,
    private val seed: Long = 0
) : SingleGameEntity() {

    var parameters: SetOfStatic2DParameters =
        SetOfStatic2DParameters(
            0f, 0f, 0f, 0f, 0f
        )

    val worldSize: Point2D
        get() {
            val w = mapComponent?.getWorldWidth() ?: 0f
            val h = mapComponent?.getWorldHeight() ?: 0f
            return Point2D(w, h)
        }

    var mapComponent: TileMap? = null
        private set(value) {
            value?.let {
                it.generateGraph(
                    // TODO: provide from procedural
                    mapPresets.walkingLayers,
                    mapPresets.obstacleLayers
                )

                field = it
            }
        }

    private val eventSet: MutableSet<AccumulatedTimeEvent> = mutableSetOf()

    var isDebugMeshEnabled: Boolean = false
    set(value) {
        mapComponent?.isDebugMeshEnabled = value
        field = value
    }

    init {
        it = CompositeEntity()
    }

    fun init(
        renderProjection: Matrix4f,
        collisionContexts: List<CollisionContext<*>>
    ) {
        parameters = SetOfStatic2DParameters(
            x = 0f,
            y = 0f,
            xSize = mapPresets.width,
            ySize = mapPresets.height,
            rotationAngle = 0f
        )

        mapComponent = if (isProcedural && proceduralGenerator != null) {
            proceduralGenerator.generateMap(seed).apply {
                shaders = TileMapGraphicsProvider.getShaders(mapPresets, renderProjection)
            }
        } else {
            TileMapGraphicsProvider.getGraphicalComponent(
                mapPresets,
                renderProjection
            )
        }

        addComponent(mapComponent, parameters)

        collisionContexts.forEach {
            it.addEntity(mapComponent as Entity, parameters)
        }

        mapComponent?.updateParameters(parameters)
        mapPresets.updateEvents.forEach {
            val event = it.invoke(mapComponent!!)
            eventSet.add(event)
        }

        isSpawned = true
    }

    override fun update(
        deltaTime: Float
    ) {
        super.update(deltaTime)
        eventSet.forEach { it.schedule(deltaTime) }
    }

    fun addToCollisionContext(collisionContext: TiledCollisionContext) {
        collisionContext.addEntity(mapComponent as Entity, parameters)
    }

    fun adjustParameters(
        sizeToMapRelation: Float,
        params: List<SetOfParameters>
    ) {
        params.forEach {
            it.xSize = (mapComponent?.getTileWidth() ?: 0f) * 3
            it.ySize = (mapComponent?.getTileHeight() ?: 0f) * 3
        }
    }

    fun retrieveObjectEntities(): List<MapObjectEntity> {
        mapComponent?.let {
            return MapObjectRetriever.getObjectsAsEntities(it)
        }

        return emptyList()
    }

    fun retrieveNonCollisionLayers(): MutableList<String> {
        return mapPresets.walkingLayers.toMutableList()
    }

    fun retrieveObjectLayers(): MutableList<String> {
        return mapPresets.obstacleLayers.toMutableList()
    }

    override fun getZLevel(): Float {
        return Float.NEGATIVE_INFINITY
    }
}