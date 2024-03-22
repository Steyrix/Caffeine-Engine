package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.feature.collision.CollisionContext
import engine.feature.tiled.data.TileMap

class TiledCollisionContext(
    private val nonCollisionLayers: MutableList<String> = mutableListOf(),
    private val objectLayers: MutableList<String> = mutableListOf(),
    val tileMap: TileMap
) : CollisionContext<TiledCollider> {

    override val colliders: MutableSet<TiledCollider> = mutableSetOf()

    override val entities: MutableSet<Entity> = mutableSetOf()

    override val entitiesParams: MutableMap<Entity, SetOfParameters> = mutableMapOf()

    override val toRemove: MutableSet<Entity> = mutableSetOf()

    override val collisions: MutableMap<TiledCollider, MutableList<Entity>> = mutableMapOf()

    override fun addCollider(collider: TiledCollider) {
        super.addCollider(collider)

        val toAddNonCollisionLayers = nonCollisionLayers.filter {
            !collider.nonCollisionLayers.contains(it)
        }

        val toAddObjectLayers = objectLayers.filter {
            !collider.objectLayers.contains(it)
        }

        collider.nonCollisionLayers.addAll(toAddNonCollisionLayers)
        collider.objectLayers.addAll(toAddObjectLayers)
    }

    override fun update() {
        val occupiedTiles = colliders.map { it.currentOccupiedTile }
        colliders.forEach { collider ->
            collider.tilesOccupiedByOtherEntities = occupiedTiles.filter {
                it != collider.currentOccupiedTile
            }
        }
        super.update()
    }
}