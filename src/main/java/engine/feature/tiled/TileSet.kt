package engine.feature.tiled

import engine.core.texture.Texture2D

class TileSet(
        internal val tileWidthPx: Float,
        internal val tileHeightPx: Float,
        internal val texture2D: Texture2D,
        private val textureTileCount: Int,
        private val textureColumnCount: Int
) {

    companion object {
        internal fun generateTiles(set: TileSet): MutableList<Tile> {
            val out = mutableListOf<Tile>()

            for (i in 0 until set.textureTileCount) {
                val uv = set.getTileUvByNumber(i)
                out.add(Tile(uv))
            }

            return out
        }
    }

    private val tiles = generateTiles(this)

    internal val relativeTileWidth = tileWidthPx / texture2D.getWidthF()
    internal val relativeTileHeight = tileHeightPx / texture2D.getHeightF()

    internal fun getTileByNumber(numberInSet: Int) = tiles[numberInSet]

    private fun getTileUvByNumber(numberInSet: Int): FloatArray {
        val x = tileWidthPx / texture2D.getWidthF()
        val y = tileHeightPx / texture2D.getHeightF()

        val currentRow = (numberInSet / textureColumnCount).toFloat()
        val currentColumn = (numberInSet % textureColumnCount).toFloat()

        return floatArrayOf(
                currentColumn * x, (currentRow + 1) * y,
                (currentColumn + 1) * x, currentRow * y,
                currentColumn * x, currentRow * y,
                currentColumn * x, (currentRow + 1) * y,
                (currentColumn + 1) * x, (currentRow + 1) * y,
                (currentColumn + 1) * x, currentRow * y
        )
    }
}