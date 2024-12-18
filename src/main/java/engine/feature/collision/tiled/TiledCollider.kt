package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.feature.collision.Collider
import engine.core.geometry.Point2D
import engine.core.update.Moving
import engine.core.update.SetOfParameters
import engine.core.update.Updatable
import engine.feature.tiled.data.TileMap

private const val EMPTY_TILE_VALUE = 0

class TiledCollider(
    override val holderEntity: Entity,
    private val parameters: SetOfParameters,
    private val map: TileMap,
    val shouldBlockTile: Boolean = false,
    val width: Int = 0,
    val height: Int = 0
) : Collider, Updatable {

    private var previousTilePos: Point2D = Point2D(parameters.x, parameters.y)

    val nonCollisionLayers: MutableList<String> = mutableListOf()
    val objectLayers: MutableList<String> = mutableListOf()

    var isOutOfMap = false
        private set

    var currentOccupiedTiles: MutableList<Int> = calculateOccupiedTiles()

    var tilesOccupiedByOtherEntities: List<Int> = emptyList()

    var shouldCheckForOccupiedTiles = true

    override fun reactToCollision() {
        parameters.x = previousTilePos.x
        parameters.y = previousTilePos.y

        (holderEntity as? Moving)?.let {
            it.horizontalVelocity = 0f
            it.verticalVelocity = 0f
        }
    }

    override fun isColliding(entity: Entity): Boolean {
        return isCollidingWithMapObjects(map)
    }

    private fun isCollidingWithMapObjects(map: TileMap): Boolean {
        val centerX = parameters.x + parameters.xSize / 2
        val centerY = parameters.y + parameters.ySize / 2
        val bottomY = parameters.y + parameters.ySize * 0.71f
        val topY = parameters.y

        var isBottomColliding = true

        nonCollisionLayers.forEach { layer ->
            if (map.getTileValue(centerX, bottomY, layer) >= EMPTY_TILE_VALUE) {
                isBottomColliding = false
            }
        }
        objectLayers.forEach { layer ->
            if (map.getTileValue(centerX, bottomY, layer) >= EMPTY_TILE_VALUE) {
                isBottomColliding = true
            }
        }

        map.processIntersectionIfNeeded(Point2D(centerX, centerY))
        map.processIntersectionIfNeeded(Point2D(centerX, bottomY))

        isOutOfMap = when {
            centerX < 0 || centerX >= map.getWorldWidth() -> true
            bottomY < 0 || topY >= map.getWorldHeight() -> true
            else -> false
        }

        if (isBottomColliding || isCollidingWithOccupiedTiles()) {
            return true
        } else {
            previousTilePos = Point2D(parameters.x, parameters.y)
        }

        return false
    }

    private fun isCollidingWithOccupiedTiles(): Boolean {
        if (!shouldCheckForOccupiedTiles) return false
        val centerX = parameters.x + parameters.xSize / 2
        val bottomY = parameters.y + parameters.ySize * 0.71f

        val tile = map.getTileIndex(centerX, bottomY)
        if (tilesOccupiedByOtherEntities.contains(tile)) {
            return true
        } else {
            previousTilePos = Point2D(parameters.x, parameters.y)
        }

        return false
    }

    override fun update(deltaTime: Float) {
        currentOccupiedTiles = calculateOccupiedTiles()
    }

    private fun calculateOccupiedTiles(): MutableList<Int> {
        val out = mutableListOf<Int>()

        for (j in 0 until  height) {
            val absHeight = map.absoluteTileHeight()
            val absWidth = map.absoluteTileWidth()
            val tileY = parameters.y + absHeight * j + absHeight / 2
            for (i in 0 until  width) {
                val tileX = parameters.x + absWidth * i + absWidth / 2
                out.add(map.getTileIndex(tileX, tileY))
            }
        }

        return out
    }
}