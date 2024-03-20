package engine.feature.collision

import engine.core.entity.Entity
import engine.core.update.SetOfParameters

interface CollisionContext<T : Collider> {

    val colliders: MutableSet<T>

    val entities: MutableSet<Entity>

    val entitiesParams: MutableMap<Entity, SetOfParameters>

    val toRemove: MutableSet<Entity>

    val collisions: MutableMap<T, MutableList<Entity>>

    fun addCollider(collider: T) {
        colliders.add(collider)
        collisions[collider] = mutableListOf()
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
                } else {
                    if (entity != collider.holderEntity && collider.isColliding(entity)) {
                        collider.reactToCollision()

                        collisions[collider]?.add(entity)
                        if (entity is CollisionReactive) {
                            entity.reactToCollision()
                        }
                    }
                }
            }
        }

        checkExitCollisions()
    }

    private fun checkExitCollisions() {
        val iterator = collisions.iterator()
        while (iterator.hasNext()) {
            val element = iterator.next()

            val innerIterator = element.value.iterator()
            while (innerIterator.hasNext()) {
                val listElement = innerIterator.next()
                if (!element.key.isColliding(listElement)) {
                    innerIterator.remove()
                }
            }
            if (element.value.isEmpty()) {
                element.key.onCollisionExit()
            }
        }
    }

    fun dispose() {
        colliders.clear()
        entities.clear()
        entitiesParams.clear()
    }
}