package engine.feature.tiled.traversing

import engine.core.entity.Entity
import java.util.Queue

class TileTraverser(
        private var currentIndex: Int,
        private val tileGraph: Map<Int, MutableList<Int>>,
        private val entity: Entity
) {

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
}