package engine.feature.tiled.traversing

import engine.core.entity.CompositeEntity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.geometry.Point2D
import engine.feature.tiled.TileMap
import kotlin.math.abs

class TileTraverser(
        private val tileGraph: Map<Int, List<Int>>,
        private val tileMap: TileMap,
        private val params: SetOf2DParametersWithVelocity
) : CompositeEntity() {

    private var currentPath: ArrayDeque<Int>? = null
    private var currentDestination: Int = -1
    private var velocity = 5f

    fun moveTo(targetPos: Point2D) {
        val start = tileMap.getTileIndex(params.x, params.y)
        val destination = tileMap.getTileIndex(targetPos.x, targetPos.y)

        if (currentDestination == destination) return
        currentDestination = destination

        currentPath = ShortestPath.pathTo(
                tileGraph,
                start,
                destination
        )
    }

    private fun traverse() {
        currentPath?.let {
            if (it.isEmpty()) return

            var node = it.first()
            while (it.isNotEmpty() && tileIsReached(node)) {
                it.removeFirst()
                if (it.isNotEmpty()) {
                    node = it.first()
                }
            }

            if (it.isNotEmpty()) {
                val nextPos = tileMap.getTilePosition(node)

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
            } else {
                params.velocityY = 0f
                params.velocityX = 0f
            }
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        traverse()
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
}