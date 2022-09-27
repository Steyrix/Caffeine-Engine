package engine.feature.tiled

data class Tile(
        val tileUV: FloatArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tile

        if (!tileUV.contentEquals(other.tileUV)) return false

        return true
    }

    override fun hashCode(): Int {
        return tileUV.contentHashCode()
    }
}