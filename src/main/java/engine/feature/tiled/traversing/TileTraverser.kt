package engine.feature.tiled.traversing

import engine.core.entity.CompositeEntity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.geometry.Point2D
import engine.feature.tiled.TileMap
import kotlin.math.abs

// todo: use single instance of tileGraph for all entities in a context
class TileTraverser(
        private val graph: TileGraph,
        private val tileMap: TileMap,
        private val params: SetOf2DParametersWithVelocity
) : CompositeEntity() {

    companion object {
        private const val INDEX_NOT_FOUND = -1
    }

    private var currentPath: ArrayDeque<Int> = ArrayDeque()
    private var currentDestination: Int = INDEX_NOT_FOUND
    private var velocity = 5f

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        traverse()
    }

    fun moveTo(targetPos: Point2D) {
        val destination = tileMap.getTileIndex(targetPos.x, targetPos.y)

        if (currentDestination == destination) return

        val start = tileMap.getTileIndex(params.x, params.y)
        currentDestination = destination
        dropVelocity()

        val nextPath = PathFinder.pathToByDijkstra(
                graph,
                start,
                destination
        )

        extendCurrentPath(nextPath)
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
        while (it.isNotEmpty() && tileIsReached(node)) {
            graph.decreaseDistance(node)
            it.removeFirst()
            if (it.isNotEmpty()) {
                node = it.first()
            }
        }

        return node
    }

    private fun modifyVelocity(tileIndex: Int) {
        val nextPos = tileMap.getTilePosition(tileIndex)

        params.velocityX = when {
            isHorizontalDiffInsignificant(nextPos.x) -> 0f
            nextPos.x > params.x -> velocity
            nextPos.x < params.x -> -velocity
            else -> 0f
        }

        params.velocityY = when {
            isVerticalDiffInsignificant(nextPos.y) -> 0f
            nextPos.y > params.y -> velocity
            nextPos.y < params.y -> -velocity
            else -> 0f
        }
    }

    private fun tileIsReached(tileIndex: Int): Boolean {
        val pos = tileMap.getTilePosition(tileIndex)

        val x = pos.x
        val isHorizontalIntersection = x in params.x..(params.x + params.xSize)

        val y = pos.y
        val isVerticalIntersection = y in params.y..(params.y + params.ySize)

        val isInsignificantDiff = isHorizontalDiffInsignificant(x) && isVerticalDiffInsignificant(y)
        return isHorizontalIntersection && isVerticalIntersection || isInsignificantDiff
    }

    private fun isHorizontalDiffInsignificant(x: Float): Boolean {
        return abs(x - params.x) < 1f
    }

    private fun isVerticalDiffInsignificant(y: Float): Boolean {
        return abs(y - params.y) < 1f
    }

    private fun dropVelocity() {
        params.velocityY = 0f
        params.velocityX = 0f
    }
}