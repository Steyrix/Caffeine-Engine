package engine.feature.tiled.traversing

import engine.core.entity.Entity

class TileTraverser(
        private var currentIndex: Int,
        private val tileGraph: Map<Int, MutableList<Int>>,
        private val entity: Entity
) {

    fun traverse() {
        // do moving
    }

    private fun getPathToTile(tileIndex: Int): List<Int> {
        // djikstra
        return emptyList()
    }
}