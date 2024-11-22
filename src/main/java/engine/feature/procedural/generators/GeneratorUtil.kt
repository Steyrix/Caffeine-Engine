package engine.feature.procedural.generators

import engine.core.geometry.Point2D
import engine.feature.tiled.data.TileSet

object GeneratorUtil {

    fun Point2D.toTileId(
        worldData: List<Point2D>
    ): Int {
        worldData.forEachIndexed { index, it ->
            if (it == this) {
                return index
            }
        }

        return 0
    }

    fun normalizeForTileSet(value: Pair<Point2D, Float?>, set: TileSet): Pair<Point2D, Int> {
        val count = set.getUniqueTilesCount()
        val setOfIds = (0 until count).toList()

        return if (value.second == null) {
            value.first to -1
        } else {
            value.first to setOfIds.random()
        }
    }
}