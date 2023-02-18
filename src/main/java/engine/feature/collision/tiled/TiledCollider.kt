package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.Collider
import engine.feature.collision.CollisionContext
import engine.feature.geometry.Point2D
import engine.feature.tiled.TileMap

class TiledCollider(
        private val parameters: SetOf2DParametersWithVelocity,
        private val collisionLayerName: String,
        override var collisionContext: CollisionContext
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
            val centerX = parameters.x + parameters.xSize / 2
            val centerY = parameters.y + parameters.ySize / 2
            val bottomY = parameters.y + parameters.ySize

            val isCenterColliding = it.getTileValue(centerX, centerY, collisionLayerName) <= 0
            val isBottomColliding = it.getTileValue(centerX, bottomY, collisionLayerName) <= 0
            if (isCenterColliding || isBottomColliding) {
                reactToCollision()
                return true
            } else {
                previousTilePos = Point2D(parameters.x, parameters.y)
            }
        }

        return false
    }
}