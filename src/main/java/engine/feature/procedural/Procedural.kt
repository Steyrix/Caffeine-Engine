package engine.feature.procedural

import engine.core.geometry.Point2D
import engine.feature.tiled.data.TileSet

object Procedural {

    fun generateMap(set: TileSet) {
        // retrieve biomes
        // should probably use different tilesets for biomes
        // and then apply partial intersection of biomes
        // use noise
    }

    private fun normalizeForTileset(
        uniqueTilesCount: Int,
        values: List<Float>
    ): List<Float> {
        return values.map { it % uniqueTilesCount }
    }

    private fun getNoiseForCoordinate(pos: Point2D): Float {
        return OpenSimplex2S.noise2_ImproveX(
            System.currentTimeMillis(), pos.x.toDouble(), pos.y.toDouble()
        )
    }
}