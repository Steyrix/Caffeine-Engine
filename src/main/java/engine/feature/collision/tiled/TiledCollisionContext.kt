package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.feature.collision.Collider
import engine.feature.collision.CollisionContext
import engine.feature.tiled.TileMap

class TiledCollisionContext : CollisionContext {

    override val colliders: MutableList<Collider> = mutableListOf()

    override val entities: MutableList<Entity> = mutableListOf()

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