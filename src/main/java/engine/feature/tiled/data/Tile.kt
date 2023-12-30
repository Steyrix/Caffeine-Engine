package engine.feature.tiled.data

data class Tile(
    val tileUV: FloatArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tile

        return tileUV.contentEquals(other.tileUV)
    }

    override fun hashCode(): Int {
        return tileUV.contentHashCode()
    }
}