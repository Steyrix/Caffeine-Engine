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
    private var velocity = 1f

    fun moveTo(targetPos: Point2D) {
        val start = tileMap.getTileIndex(params.x, params.y)
        val destination = tileMap.getTileIndex(targetPos.x, targetPos.y)

        if (currentDestination == destination) return
        println("Recreating the path ---------------------------------------------------")
        currentDestination = destination

        currentPath = ShortestPath.pathTo(
                tileGraph,
                start,
                destination
        )
        println("Current path: $currentPath")
        println("Dest pos $destination: ${targetPos.x}, ${targetPos.y}")
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
                println(it)
            }

            if (it.isNotEmpty()) {
                println(node)
                val nextPos = tileMap.getTilePosition(node)
                println("Curr pos: ${params.x} , ${params.y}")
                println("Next pos of $node: ${nextPos.x} , ${nextPos.y}")

                params.velocityX = when {
                    hasReachedX(node) -> 0f
                    nextPos.x > params.x -> velocity
                    nextPos.x < params.x -> -velocity
                    else -> 0f
                }

                params.velocityY = when {
                    hasReachedY(node) -> 0f
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
        return hasReachedX(tileIndex) && hasReachedY(tileIndex)
    }

    private fun hasReachedX(tileIndex: Int): Boolean {
        val x = tileMap.getTilePosition(tileIndex).x
        val isHorizontalIntersection = x in params.x..(params.x + params.xSize)
        val isHorizontalDiffInsignificant = abs(x - params.x) < 1f
        return isHorizontalIntersection || isHorizontalDiffInsignificant
    }

    private fun hasReachedY(tileIndex: Int): Boolean {
        val y = tileMap.getTilePosition(tileIndex).y
        val isVerticalIntersection = y in params.y..(params.y + params.ySize)
        val isVerticalDiffInsignificant = abs(y - params.y) < 1f
        return isVerticalIntersection || isVerticalDiffInsignificant
    }
}