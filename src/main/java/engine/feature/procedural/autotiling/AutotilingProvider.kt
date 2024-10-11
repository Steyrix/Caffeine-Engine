package engine.feature.procedural.autotiling

class AutotilingProvider(
    private val tileAdjacencyMap: HashMap<Int, Adjacent>
) {

    fun getAdjacentTiles(id: Int): Adjacent? {
        return tileAdjacencyMap[id]
    }

    fun getNorthTileFor(id: Int): Int {
        return tileAdjacencyMap[id]?.northId ?: - 1
    }

    fun getWestTileFor(id: Int): Int {
        return tileAdjacencyMap[id]?.westId ?: - 1
    }

    fun getSouthTileFor(id: Int): Int {
        return tileAdjacencyMap[id]?.southId ?: - 1
    }

    fun getEastTileFor(id: Int): Int {
        return tileAdjacencyMap[id]?.eastId ?: - 1
    }
}