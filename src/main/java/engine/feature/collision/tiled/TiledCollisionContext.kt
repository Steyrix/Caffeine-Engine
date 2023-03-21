package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.feature.collision.Collider
import engine.feature.collision.CollisionContext
import engine.feature.tiled.TileMap

class TiledCollisionContext : CollisionContext {

    override val colliders: MutableSet<Collider> = mutableSetOf()

    override val entities: MutableSet<Entity> = mutableSetOf()

    override val entitiesParams: MutableMap<Entity, SetOfParameters> = mutableMapOf()
    override fun addCollider(collider: Collider) {
        if (collider !is TiledCollider) return
        super.addCollider(collider)
    }

    fun getMap(): TileMap? {
        entities.forEach {
            if (it is TileMap) return it
        }

        return null
    }
}