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

    fun removeEntity(entity: Entity) {
        entities.remove(entity)
    }

    // todo N^2 optimize
    fun update() {
        colliders.forEach { collider ->
            entities.forEach { entity ->
                if (collider.isColliding(entity)) {
                    collider.reactToCollision()

                    if (entity is CollisionReactive) {
                        entity.reactToCollision()
                    }
                }
            }
        }
    }
}