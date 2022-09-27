package engine.feature.tiled

object TilesUtil {
    internal fun rotateTiles(tiles: List<Tile>, columnCount: Int): MutableList<Tile> {
        val out = mutableListOf<Tile>()
        for (i in tiles.indices step columnCount) {
            val temp = mutableListOf<Tile>()
            temp.addAll(tiles.subList(i, i + columnCount))
            temp.reverse()

            out.addAll(temp)
        }

        return out.reversed().toMutableList()
    }
}