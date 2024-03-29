package engine.feature.collision

import engine.core.entity.Entity

interface Collider : Entity, CollisionReactive {

    fun isColliding(entity: Entity): Boolean

    val holderEntity: Entity
}