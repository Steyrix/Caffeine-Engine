package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.Collider
import engine.feature.tiled.TileMap

// todo identify which tile is previous
// todo return to previous tile as reaction to collision
class TiledCollider(
        private val parameters: SetOf2DParametersWithVelocity,
        private val collisionLayerName: String
) : Collider {

    override fun reactToCollision() {
        TODO("Not yet implemented")
    }

    override fun isColliding(entity: Entity): Boolean {
        (entity as? TileMap)?.let {
            if (it.getTileIndexInLayer(parameters.x, parameters.y, collisionLayerName) != -1) {
                reactToCollision()
                return true
            }
        }

        return false
    }
}