package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.feature.collision.CollisionContext

class TiledCollisionContext(
    private val nonCollisionLayers: MutableList<String> = mutableListOf(),
    private val objectLayers: MutableList<String> = mutableListOf()
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
}