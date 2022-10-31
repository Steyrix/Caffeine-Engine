package engine.feature.collision.boundingbox

import engine.core.entity.Entity
import engine.feature.collision.Collider
import engine.feature.collision.CollisionContext

class BoundingBoxCollisionContext : CollisionContext {

    override val colliders: MutableList<Collider> = mutableListOf()

    override val entities: MutableList<Entity> = mutableListOf()

    override fun addCollider(collider: Collider) {
        if (collider !is BoundingBoxCollider) return
        super.addCollider(collider)
    }

    override fun addEntity(entity: Entity) {
        if (entity !is BoundingBox) return
        super.addEntity(entity)
    }

    // todo N^2 optimize
    override fun update() {
        colliders.forEach { collider ->
            entities.forEach { entity ->
                if (collider.isColliding(entity)) collider.reactToCollision()
            }
        }
    }
}