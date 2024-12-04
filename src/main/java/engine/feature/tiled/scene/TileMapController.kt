package engine.feature.tiled.scene

import engine.core.entity.Entity
import engine.core.geometry.Point2D
import engine.core.loop.GameLoopTimeEvent
import engine.core.update.SetOfStaticParameters
import engine.feature.collision.CollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.`object`.MapObjectEntity
import engine.feature.tiled.data.`object`.MapObjectRetriever
import org.joml.Matrix4f

interface TileMapController {

    var parameters: SetOfStaticParameters

    val worldSize: Point2D

    var mapComponent: TileMap?

    var isDebugMeshEnabled: Boolean

    val eventSet: MutableSet<GameLoopTimeEvent>

    val walkableLayers: MutableList<String>

    val obstacleLayers: MutableList<String>

    fun init(
        renderProjection: Matrix4f,
        collisionContexts: List<CollisionContext<*>>
    )

    fun addToCollisionContext(collisionContext: TiledCollisionContext) {
        collisionContext.addEntity(mapComponent as Entity, parameters)
    }

    fun retrieveObjectEntities(): List<MapObjectEntity> {
        mapComponent?.let {
            return MapObjectRetriever.getObjectsAsEntities(it)
        }

        return emptyList()
    }
}