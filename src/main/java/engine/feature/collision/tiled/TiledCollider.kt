package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.Collider
import engine.feature.collision.CollisionContext
import engine.feature.geometry.Point2D
import engine.feature.tiled.data.TileMap

private const val EMPTY_TILE_VALUE = 0

class TiledCollider(
        override val holderEntity: Entity,
        private val parameters: SetOf2DParametersWithVelocity,
        private val nonCollisionLayers: List<String>,
        override var collisionContext: CollisionContext
) : Collider {

    private var previousTilePos: Point2D = Point2D(parameters.x, parameters.y)

    override fun reactToCollision() {
        parameters.x = previousTilePos.x
        parameters.y = previousTilePos.y
        parameters.velocityX = 0f
        parameters.velocityY = 0f
    }

    override fun isColliding(entity: Entity): Boolean {
        return if (entity is TileMap) {
            isCollidingWithMapObjects(entity)
        } else {
            false
        }
    }

    private fun isCollidingWithMapObjects(map: TileMap): Boolean {
        val centerX = parameters.x + parameters.xSize / 2
        val centerY = parameters.y + parameters.ySize / 2
        val bottomY = parameters.y + parameters.ySize

        var isCenterColliding = true
        var isBottomColliding = true

        nonCollisionLayers.forEach { layer ->
            if (map.getTileValue(centerX, centerY, layer) >= EMPTY_TILE_VALUE) isCenterColliding = false
            if (map.getTileValue(centerX, bottomY, layer) >= EMPTY_TILE_VALUE) isBottomColliding = false
        }

        val isOutOfMap = when {
//            centerX < 0 || centerX >= mapParameters.xSize -> true
//            centerY < 0 || centerY >= mapParameters.ySize -> true
            else -> false
        }

        if (isCenterColliding || isBottomColliding || isOutOfMap) {
            return true
        } else {
            previousTilePos = Point2D(parameters.x, parameters.y)
        }

        return false
    }
}