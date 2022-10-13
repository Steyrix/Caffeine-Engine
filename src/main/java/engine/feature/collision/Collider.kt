package engine.feature.collision

import engine.core.entity.Entity

interface Collider {
    fun reactToCollision()

    fun isColliding(entity: Entity): Boolean
}