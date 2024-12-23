package engine.feature.tiled.scene

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.game_object.SingleGameEntity
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStaticParameters
import engine.feature.collision.CollisionContext
import engine.core.geometry.Point2D
import engine.core.loop.GameLoopTimeEvent
import engine.feature.tiled.data.TileMap
import org.joml.Matrix4f

class PresetTileMap(
    private val mapPresets: TileMapPreset
) : SingleGameEntity(), TileMapController {

    override var parameters: SetOfStaticParameters =
        SetOfStaticParameters(
            0f, 0f, 0f, 0f, 0f
        )

    override val worldSize: Point2D
        get() {
            val w = mapComponent?.getWorldWidth() ?: 0f
            val h = mapComponent?.getWorldHeight() ?: 0f
            return Point2D(w, h)
        }

    override val walkableLayers = mapPresets.walkingLayers.toMutableList()
    override val obstacleLayers = mapPresets.obstacleLayers.toMutableList()

    override var mapComponent: TileMap? = null
        set(value) {
            value?.let {
                it.generateGraph(
                    mapPresets.walkingLayers,
                    mapPresets.obstacleLayers
                )

                field = it
            }
        }

    override val eventSet: MutableSet<GameLoopTimeEvent> = mutableSetOf()

    override var isDebugMeshEnabled: Boolean = false
    set(value) {
        mapComponent?.isDebugMeshEnabled = value
        field = value
    }

    init {
        it = CompositeEntity()
    }

    override fun init(
        renderProjection: Matrix4f,
        collisionContexts: List<CollisionContext<*>>
    ) {
        parameters = SetOfStaticParameters(
            x = 0f,
            y = 0f,
            xSize = mapPresets.width,
            ySize = mapPresets.height,
            rotationAngle = 0f
        )

        mapComponent =
            TileMapGraphicsProvider.getGraphicalComponent(
                mapPresets,
                renderProjection
            )

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

    fun adjustParameters(
        sizeToMapRelation: Float,
        params: List<SetOfParameters>
    ) {
        params.forEach {
            it.xSize = (mapComponent?.getTileWidth() ?: 0f) * 3
            it.ySize = (mapComponent?.getTileHeight() ?: 0f) * 3
        }
    }

    override fun getZLevel(): Float {
        return Float.NEGATIVE_INFINITY
    }
}