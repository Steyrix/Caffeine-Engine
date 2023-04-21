package engine.feature.tiled.traversing

import engine.core.entity.CompositeEntity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.getCenterPoint
import engine.feature.geometry.Point2D
import engine.feature.tiled.TileMap
import kotlin.math.abs

// TODO: avoid obstacles
class TileTraverser(
        private val graph: TileGraph,
        private val tileMap: TileMap,
        private val params: SetOf2DParametersWithVelocity
) : CompositeEntity() {

    companion object {
        private const val INDEX_NOT_FOUND = -1
        private const val VELOCITY = 5f
        private const val INSIGNIFICANT_DIFFERENCE = 4f
    }

    private var currentPath: ArrayDeque<Int> = ArrayDeque()
    private var currentDestination: Int = INDEX_NOT_FOUND

    private var currentTile = -1
    private var previousTile = -1

    private var isPaused = false

    fun resume() {
        isPaused = false
    }

    fun pause() {
        isPaused = true
        dropVelocity()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        if (!isPaused) traverse()
    }

    fun moveTo(targetPos: Point2D) {
        val destination = tileMap.getTileIndex(targetPos.x, targetPos.y)

        if (currentDestination == destination) return

        val entityCenter = params.getCenterPoint()
        val start = tileMap.getTileIndex(entityCenter.x, entityCenter.y)
        currentTile = start

        currentDestination = destination
        dropVelocity()

        val nextPath = PathFinder.pathToByDijkstra(
                graph,
                start,
                destination
        )

        extendCurrentPath(nextPath)
        println(currentPath)
    }

    private fun extendCurrentPath(nextPath: ArrayDeque<Int>) {
        if (nextPath.isNotEmpty()) {

            val indexOfJoin = if (currentPath.isEmpty()) {
                INDEX_NOT_FOUND
            } else {
                nextPath.indexOf(currentPath.last())
            }

            if (indexOfJoin != -1) {
                val toAdd = nextPath.filterIndexed { index, _ -> index > indexOfJoin }
                currentPath.addAll(toAdd)
            } else {
                currentPath.clear()
                currentPath.addAll(nextPath)
            }
        }
    }

    private fun traverse() {
        if (currentPath.isEmpty()) {
            dropVelocity()
            return
        }

        val node = getActualNode(currentPath)

        if (currentPath.isNotEmpty()) {
            modifyVelocity(node)
        } else {
            dropVelocity()
        }
    }

    private fun getActualNode(it: ArrayDeque<Int>): Int {
        var node = it.first()
        while (it.isNotEmpty() && tileIsReachedV2(node)) {
            previousTile = currentTile
            currentTile = node
            graph.decreaseCost(previousTile)

            it.removeFirst()
            if (it.isNotEmpty()) {
                node = it.first()
            }
        }

        return node
    }

    private fun modifyVelocity(tileIndex: Int) {
        val nextPos = tileMap.getTilePosition(tileIndex)
        val x = nextPos.x + tileMap.getTileWidth() / 2
        val y = nextPos.y + tileMap.getTileHeight() / 2

        val centerPoint = params.getCenterPoint()
        val bottomPoint = params.y + params.ySize

        params.velocityX = when {
            isHorizontalDiffInsignificant(x) -> 0f
            x > centerPoint.x -> VELOCITY
            x < centerPoint.x -> -VELOCITY
            else -> 0f
        }

        params.velocityY = when {
            isVerticalDiffInsignificant(y) -> 0f
            y > centerPoint.y -> VELOCITY
            y < centerPoint.y -> -VELOCITY
            else -> 0f
        }
    }

    private fun tileIsReached(tileIndex: Int): Boolean {
        val pos = tileMap.getTilePosition(tileIndex)
        val tilePosX = pos.x
        val tilePosY = pos.y

        val entityCenter = params.getCenterPoint()
        val x = entityCenter.x
        val y = entityCenter.y

        val isHorizontalIntersection = x in tilePosX..(tilePosX + tileMap.getTileWidth())
        val isVerticalIntersection = y in tilePosY..(tilePosY + tileMap.getTileHeight())

        val isInsignificantDiff = isHorizontalDiffInsignificant(tilePosX) && isVerticalDiffInsignificant(tilePosY)
        return isHorizontalIntersection && isVerticalIntersection || isInsignificantDiff
    }

    private fun tileIsReachedV2(tileIndex: Int): Boolean {
        val pos = tileMap.getTilePosition(tileIndex)
        val tilePosX = pos.x + tileMap.getTileWidth() /2
        val tilePosY = pos.y + tileMap.getTileHeight() / 2

        val entityCenter = params.getCenterPoint()
        val x = entityCenter.x
        val y = entityCenter.y
        val bottomY = params.y + params.ySize

        val isHorizontalIntersection = x in (tilePosX - 1f)..(tilePosX + 1f)
        val isVerticalIntersection = y in (tilePosY - 1f)..(tilePosX + 1f)
        val isBottomVerticalInterscetion = bottomY in (tilePosY - 1f)..(tilePosY + 1f)
        val isCenterIntersection = isHorizontalIntersection && isVerticalIntersection
        val isBottomInterscetion = isBottomVerticalInterscetion && isHorizontalIntersection

        val isInsignificantDiff = isHorizontalDiffInsignificant(tilePosX) && isVerticalDiffInsignificant(tilePosY)
        return isCenterIntersection || isBottomInterscetion || isInsignificantDiff
    }

    private fun isHorizontalDiffInsignificant(x: Float): Boolean {
        return abs(x - params.x - params.xSize / 2) <= INSIGNIFICANT_DIFFERENCE
    }

    private fun isVerticalDiffInsignificant(y: Float): Boolean {
        return abs(y - params.y - params.ySize / 2) <= INSIGNIFICANT_DIFFERENCE
    }

    private fun dropVelocity() {
        params.velocityY = 0f
        params.velocityX = 0f
    }
}