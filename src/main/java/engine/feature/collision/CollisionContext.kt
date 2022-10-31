package engine.feature.collision

import engine.core.entity.Entity

interface CollisionContext {

    val colliders: MutableList<Collider>

    val entities: MutableList<Entity>

    fun addCollider(collider: Collider) {
        colliders.add(collider)
    }

    fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    fun update()
}