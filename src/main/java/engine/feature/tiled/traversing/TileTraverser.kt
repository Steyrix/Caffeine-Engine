package engine.feature.tiled.traversing

import engine.core.entity.CompositeEntity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.tiled.TileMap

class TileTraverser(
        private var currentIndex: Int,
        private val tileGraph: Map<Int, MutableList<Int>>,
        private val tileMap: TileMap,
        private val params: SetOf2DParametersWithVelocity
): CompositeEntity() {

    private var currentPath: ArrayDeque<Int>? = null

    fun moveTo(tileIndex: Int) {
        currentPath = getPathToTile(tileIndex)
    }

    fun traverse() {
        currentPath?.let {
            if (it.last() == currentIndex || it.isEmpty()) return

            // todo determine in which way to move to reach next
            val next = it.first()
            params.velocityX = 0f
        }
        // do moving
    }

    private fun getPathToTile(tileIndex: Int): ArrayDeque<Int> {
        // djikstra
        return ArrayDeque()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        traverse()
    }
}