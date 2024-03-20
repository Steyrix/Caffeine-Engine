package engine.feature.collision

import engine.core.entity.Entity
import engine.core.update.SetOfParameters

interface CollisionContext {

    val colliders: MutableSet<Collider>

    val entities: MutableSet<Entity>

    val entitiesParams: MutableMap<Entity, SetOfParameters>

    val toRemove: MutableSet<Entity>

    fun addCollider(collider: Collider) {
        colliders.add(collider)
    }

    fun addEntity(entity: Entity, parameters: SetOfParameters) {
        entities.add(entity)
        entitiesParams[entity] = parameters
    }

    fun removeEntity(entity: Entity) {
        toRemove.add(entity)
    }

    fun containsEntity(entity: Entity): Boolean {
        return entities.contains(entity)
    }

    fun getParams(entity: Entity): SetOfParameters? {
        return entitiesParams[entity]
    }

    fun update() {
        colliders.forEach { collider ->
            val iterator = entities.iterator()
            while (iterator.hasNext()) {
                val entity = iterator.next()
                if (toRemove.contains(entity)) {
                    iterator.remove()
                    entitiesParams.remove(entity)
                }

                if (entity != collider.holderEntity && collider.isColliding(entity)) {
                    collider.reactToCollision()

                    if (entity is CollisionReactive) {
                        entity.reactToCollision()
                    }
                }
            }
        }
    }

    fun dispose() {
        colliders.clear()
        entities.clear()
        entitiesParams.clear()
    }
}