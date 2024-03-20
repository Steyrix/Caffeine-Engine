package engine.feature.collision.boundingbox

import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.feature.collision.Collider
import engine.feature.collision.CollisionContext

class BoundingBoxCollisionContext : CollisionContext {

    override val colliders: MutableSet<Collider> = mutableSetOf()

    override val entities: MutableSet<Entity> = mutableSetOf()

    override val entitiesParams: MutableMap<Entity, SetOfParameters> = mutableMapOf()

    override val toRemove: MutableSet<Entity> = mutableSetOf()

    override val collisions: MutableMap<Collider, MutableList<Entity>> = mutableMapOf()

    override fun addCollider(collider: Collider) {
        if (collider !is BoundingBoxCollider) return
        super.addCollider(collider)
    }

    override fun addEntity(entity: Entity, params: SetOfParameters) {
        if (entity !is BoundingBox) return
        super.addEntity(entity, params)
    }
}