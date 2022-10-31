package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.Collider
import engine.feature.geometry.Point2D
import engine.feature.tiled.TileMap

class TiledCollider(
        private val parameters: SetOf2DParametersWithVelocity,
        private val collisionLayerName: String
) : Collider {

    private var previousTilePos: Point2D? = null
    override fun reactToCollision() {
        parameters.x = previousTilePos?.x ?: parameters.x
        parameters.y = previousTilePos?.y ?: parameters.y
        parameters.velocityX = 0f
        parameters.velocityY = 0f
    }

    override fun isColliding(entity: Entity): Boolean {
        (entity as? TileMap)?.let {
            if (it.getTileIndexInLayer(parameters.x, parameters.y, collisionLayerName) != -1) {
                reactToCollision()
                return true
            } else {
                previousTilePos = Point2D(parameters.x, parameters.y)
            }
        }

        return false
    }
}