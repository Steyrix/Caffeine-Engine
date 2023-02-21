package engine.feature.tiled.traversing

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
class TileTraverser(
        private var currentIndex: Int,
        private val tileGraph: Map<Int, MutableList<Int>>,
        private val entity: Entity
): CompositeEntity() {

    private var currentPath: ArrayDeque<Int>? = null

    fun moveTo(tileIndex: Int) {
        currentPath = getPathToTile(tileIndex)
    }

    fun traverse() {
        // do moving
    }

    private fun getPathToTile(tileIndex: Int): ArrayDeque<Int> {
        // djikstra
        return ArrayDeque()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
    }
}