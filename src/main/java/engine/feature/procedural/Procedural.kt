package engine.feature.procedural

import engine.core.geometry.Point2D
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.data.layer.TileLayer

object Procedural {

    fun generateLayer(
        set: TileSet,
        map: TileMap,
        widthInTiles: Int = 64,
        heightInTiles: Int = 64,
        tilesCount: Int
    ): TileLayer {
        val rawList = mutableListOf<Float>()
        for (i in 0 until tilesCount) {
            rawList.add(
                getNoiseForCoordinate(
                    map.getTilePosition(i)
                )
            )
        }

        val normalizedList = normalizeForTileset(
            set.getUniqueTilesCount(),
            rawList
        ).toMutableList()

        return TileLayer(
            "",
            widthInTiles,
            heightInTiles,
            set,
            normalizedList,
            mutableListOf()
        )
    }

    private fun normalizeForTileset(
        uniqueTilesCount: Int,
        values: List<Float>
    ): List<Int> {
        return values.map { (it % uniqueTilesCount).toInt() }
    }

    private fun getNoiseForCoordinate(pos: Point2D): Float {
        return OpenSimplex2S.noise2_ImproveX(
            System.currentTimeMillis(), pos.x.toDouble(), pos.y.toDouble()
        )
    }
}