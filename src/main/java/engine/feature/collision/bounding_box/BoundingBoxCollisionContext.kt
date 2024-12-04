package engine.feature.collision.bounding_box

import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.feature.collision.CollisionContext

class BoundingBoxCollisionContext : CollisionContext<BoundingBoxCollider> {

    override val colliders: MutableSet<BoundingBoxCollider> = mutableSetOf()

    override val entities: MutableSet<Entity> = mutableSetOf()

    override val entitiesParams: MutableMap<Entity, SetOfParameters> = mutableMapOf()

    override val toRemove: MutableSet<Entity> = mutableSetOf()

    override val collisions: MutableMap<BoundingBoxCollider, MutableList<Entity>> = mutableMapOf()

    override fun addEntity(entity: Entity, params: SetOfParameters) {
        if (entity !is BoundingBox) return
        super.addEntity(entity, params)
    }
}