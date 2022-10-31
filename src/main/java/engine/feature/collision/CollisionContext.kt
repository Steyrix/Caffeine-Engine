package engine.feature.collision

import engine.core.entity.Entity

interface CollisionContext {

    val entities: MutableList<Entity>

    fun add(entity: Entity) {
        entities.add(entity)
    }

    fun update()
}