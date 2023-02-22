package engine.feature.tiled.traversing

import engine.core.entity.CompositeEntity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.tiled.TileMap

class TileTraverser(
        private var lastIndex: Int,
        private val tileGraph: Map<Int, List<Int>>,
        private val tileMap: TileMap,
        private val params: SetOf2DParametersWithVelocity
): CompositeEntity() {

    private var currentPath: ArrayDeque<Int>? = null

    fun moveTo(tileIndex: Int) {
        val start = tileMap.getTileIndex(params.x, params.y)

        currentPath = ShortestPath.pathTo(
                tileGraph,
                start,
                tileIndex
        )
    }

    private fun traverse() {
        currentPath?.let {
            if (it.last() == lastIndex || it.isEmpty()) return

            val currentIndex = tileMap.getTileIndex(params.x, params.y)
            if(currentIndex != lastIndex) {
                it.removeFirst()
                lastIndex = currentIndex

                if (it.isNotEmpty()) {
                    val nextPos = tileMap.getTilePosition(it.first())

                    if (nextPos.x > params.x) {
                        params.velocityX = 10f
                    } else {
                        params.velocityX = 0f
                    }

                    if (nextPos.y > params.y) {
                        params.velocityY = 10f
                    } else {
                        params.velocityY = 0f
                    }
                }
            }
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        traverse()
    }
}