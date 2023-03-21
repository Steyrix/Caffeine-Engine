package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.feature.collision.Collider
import engine.feature.collision.CollisionContext
import engine.feature.tiled.TileMap

class TiledCollisionContext : CollisionContext {

    override val colliders: MutableSet<Collider> = mutableSetOf()

    override val entities: MutableSet<Entity> = mutableSetOf()

    override fun addCollider(collider: Collider) {
        if (collider !is TiledCollider) return
        super.addCollider(collider)
    }

    override fun addEntity(entity: Entity) {
        if (entity !is TileMap) return
        entities.clear()
        super.addEntity(entity)
    }
}