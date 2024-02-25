package engine.feature.tiled.traversing

import engine.core.entity.CompositeEntity
import engine.core.geometry.Point2D
import engine.core.loop.PredicateTimeEvent
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.getCenterPoint
import engine.feature.tiled.data.TileMap
import kotlin.math.abs

class TileTraverser(
    private val graph: TileGraph,
    private val tileMap: TileMap,
    private val holderParams: SetOf2DParametersWithVelocity,
    private val targetParams: SetOf2DParametersWithVelocity
) : CompositeEntity() {

    companion object {
        private const val INDEX_NOT_FOUND = -1
        private const val VELOCITY = 5f
        private const val INSIGNIFICANT_DIFFERENCE = 2f
    }

    private val startChasing = PredicateTimeEvent(
        timeLimit = 2.5f,
        predicate = { (!isPaused) },
        action = {
            moveToTarget()
        }
    )

    private var currentPath: ArrayDeque<Int> = ArrayDeque()
    private var currentDestination: Int = INDEX_NOT_FOUND

    private var currentTile = -1
    private var previousTile = -1

    private var isPaused = false

    private val stumbleLimit = 10
    private var stumbleCount = 0
    private var isStumble = false

    fun resume() {
        isPaused = false
    }

    fun pause() {
        isPaused = true
        dropVelocity()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        startChasing.schedule(deltaTime)
        if (!isPaused) traverse()
    }

    fun moveToTarget() {
        println("move to target")
        val destCenter = targetParams.getCenterPoint()

        val possibleTargetCoords = listOf(
            Point2D(destCenter.x, destCenter.y),
            Point2D(destCenter.x, targetParams.y),
            Point2D(destCenter.x, targetParams.y + targetParams.ySize),
        )

        val destinations = mutableListOf<Int>()
        possibleTargetCoords.forEach {
            val result = tileMap.getTileIndex(it.x, it.y)
            if (result != -1) destinations.add(result)
        }

        if (!isStumble) {
            val entityCenter = holderParams.getCenterPoint()
            val start = tileMap.getTileIndex(entityCenter.x, entityCenter.y)
            currentTile = start
        }

        var currDestIndex = 0
        var nextPath = ArrayDeque<Int>()
        while (nextPath.isEmpty() && currDestIndex < destinations.size) {
            println("Curr dest: ${destinations[currDestIndex]}")
            currentDestination = destinations[currDestIndex]
            dropVelocity()
            nextPath = PathFinder.pathToByDijkstra(
                graph,
                currentTile,
                currentDestination
            )
            currDestIndex++
        }

        extendCurrentPath(nextPath)
    }

    private fun extendCurrentPath(nextPath: ArrayDeque<Int>) {
        println("Curr tile: ${tileMap.getTileIndex(holderParams.getCenterPoint().x, holderParams.getCenterPoint().y)}")
        println("Next path: $nextPath")
        println("Curr path: $currentPath")
        if (nextPath.isNotEmpty()) {
            isStumble = false
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
        } else if (isStumble) {
            solveStumbling()
        }

        if (currentPath.isEmpty()) {

        }
    }

    private fun solveStumbling() {
        var i = 0

        val positionsList = listOf(
            Point2D(holderParams.x, holderParams.y),
            Point2D(holderParams.x, holderParams.y + holderParams.ySize),
            Point2D(holderParams.x + holderParams.xSize, holderParams.y),
            Point2D(holderParams.x + holderParams.xSize, holderParams.y + holderParams.ySize)
        )

        while (graph.nodes[currentTile].isNullOrEmpty() && i < 4) {
            currentTile = tileMap.getTileIndex(positionsList[i].x, positionsList[i].y)
            i++
        }
    }

    private fun traverse() {
        if (currentPath.isEmpty()) {
            detectIfStumble()
            dropVelocity()
            return
        }

        val node = getActualNode(currentPath)

        if (currentPath.isNotEmpty()) {
            modifyVelocity(node)
            isStumble = false
            stumbleCount = 0
        } else {
            detectIfStumble()
            dropVelocity()
        }
    }

    private fun detectIfStumble() {
        if (stumbleCount < stumbleLimit) {
            stumbleCount++
        } else {
            isStumble = true
        }
    }

    private fun getActualNode(it: ArrayDeque<Int>): Int {
        var node = it.first()
        while (it.isNotEmpty() && tileIsReached(node)) {
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
        val bottomY = nextPos.y + tileMap.getTileHeight()

        val centerPoint = holderParams.getCenterPoint().x
        val bottomPoint = holderParams.y + holderParams.ySize

        holderParams.velocityX = when {
            isHorizontalDiffInsignificant(x) -> 0f
            x > centerPoint -> VELOCITY
            x < centerPoint -> -VELOCITY
            else -> 0f
        }

        holderParams.velocityY = when {
            isVerticalDiffInsignificant(bottomY) -> 0f
            bottomY > bottomPoint -> VELOCITY
            bottomY < bottomPoint -> -VELOCITY
            else -> 0f
        }
    }

    private fun tileIsReached(tileIndex: Int): Boolean {
        val pos = tileMap.getTilePosition(tileIndex)
        val tilePosX = pos.x + tileMap.getTileWidth() / 2
        val tilePosBottomY = pos.y + tileMap.getTileHeight()

        val x = holderParams.getCenterPoint().x
        val bottomY = holderParams.y + holderParams.ySize

        val isHorizontalIntersection = x in (tilePosX - 1f)..(tilePosX + 1f)
        val isBottomIntersection = bottomY in (tilePosBottomY - 1f)..(tilePosBottomY + 1f)

        val isInsignificantDiff = isHorizontalDiffInsignificant(tilePosX) && isVerticalDiffInsignificant(tilePosBottomY)
        return (isHorizontalIntersection && isBottomIntersection) || isInsignificantDiff
    }

    private fun isHorizontalDiffInsignificant(x: Float): Boolean {
        return abs(x - holderParams.x - holderParams.xSize / 2) <= INSIGNIFICANT_DIFFERENCE
    }

    private fun isVerticalDiffInsignificant(y: Float): Boolean {
        return abs(y - holderParams.y - holderParams.ySize) <= INSIGNIFICANT_DIFFERENCE
    }

    private fun dropVelocity() {
        holderParams.velocityY = 0f
        holderParams.velocityX = 0f
    }
}