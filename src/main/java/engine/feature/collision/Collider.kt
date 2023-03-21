package engine.feature.collision

import engine.core.entity.Entity

interface Collider : Entity, CollisionReactive {

    fun isColliding(entity: Entity): Boolean

    var collisionContext: CollisionContext

    override fun onAdd() {
        collisionContext.addCollider(this)
    }
}