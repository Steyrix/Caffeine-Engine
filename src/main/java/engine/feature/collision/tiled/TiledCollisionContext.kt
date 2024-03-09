package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.feature.collision.Collider
import engine.feature.collision.CollisionContext

class TiledCollisionContext(
    private val nonCollisionLayers: MutableList<String> = mutableListOf(),
    private val objectLayers: MutableList<String> = mutableListOf()
) : CollisionContext {

    override val colliders: MutableSet<Collider> = mutableSetOf()

    override val entities: MutableSet<Entity> = mutableSetOf()

    override val entitiesParams: MutableMap<Entity, SetOfParameters> = mutableMapOf()

    override fun addCollider(collider: Collider) {
        if (collider !is TiledCollider) return
        super.addCollider(collider)

        if (collider is TiledCollider) {
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
}