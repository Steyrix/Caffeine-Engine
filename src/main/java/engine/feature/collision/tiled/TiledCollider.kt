package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.Collider
import engine.feature.collision.CollisionContext
import engine.feature.geometry.Point2D
import engine.feature.tiled.TileMap

class TiledCollider(
        private val parameters: SetOf2DParametersWithVelocity,
        private val collisionLayersNames: List<String>,
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

            var isCenterColliding = false
            var isBottomColliding = false

            collisionLayersNames.forEach { layer ->
                if (it.getTileValue(centerX, centerY, layer) <= 0) isCenterColliding = true
                if (it.getTileValue(centerX, bottomY, layer) <= 0) isBottomColliding = true
            }

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