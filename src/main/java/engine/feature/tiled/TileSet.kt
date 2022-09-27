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

            return TilesUtil.rotateTiles(out, set.textureColumnCount)
        }
    }

    private val tiles = generateTiles(this)

    internal val relativeTileWidth = tileWidthPx / texture2D.getWidthF()
    internal val relativeTileHeight = tileHeightPx / texture2D.getHeightF()

    internal fun getTileByNumber(numberInSet: Int) = tiles[numberInSet]

    private fun getTileUvByNumber(numberInSet: Int): FloatArray {
        val currentRow = (numberInSet / textureColumnCount).toFloat()
        val currentColumn = (numberInSet % textureColumnCount).toFloat()

        return floatArrayOf(
                currentColumn * relativeTileWidth, currentRow * relativeTileHeight,
                (currentColumn + 1) * relativeTileWidth, (currentRow + 1) * relativeTileHeight,
                currentColumn * relativeTileWidth, (currentRow + 1) * relativeTileHeight,
                currentColumn * relativeTileWidth, currentRow * relativeTileHeight,
                (currentColumn + 1) * relativeTileWidth, currentRow * relativeTileHeight,
                (currentColumn + 1) * relativeTileWidth, (currentRow + 1) * relativeTileHeight
        )
    }
}