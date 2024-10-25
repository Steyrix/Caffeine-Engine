package engine.feature.procedural.generators

import engine.core.geometry.Point2D
import engine.feature.tiled.data.TileSet

object GeneratorUtil {

    /**
     * This method maps noise values to tileset tiles' ids.
     * Firstly it creates a list of available ids. After that each noise value is mapped to a random id.
     *
     * @param values is a list of points mapped to noise values, where each point is unique and refers to the start
     * (top-left corner position) of the tile.
     * @param set is a target tileset, which will be used as a source for rendering a layer.
     */
    fun normalizeForTileSet(values: List<Pair<Point2D, Float>>, set: TileSet): NormalizedData {
        val count = set.getUniqueTilesCount()
        val setOfIds = (0 until count).toList()

        val valueMap = hashMapOf<Float, Pair<Point2D, Int>>()
        val result: NormalizedData = mutableListOf()

        values.forEach {
            val noisedValue = it.second
            if (!valueMap.contains(noisedValue)) {
                valueMap[noisedValue] = it.first to setOfIds.random()
            }
        }

        for (i in values.indices) {
            result.add(
                valueMap[values[i].second]!!
            )
        }

        return result
    }
}