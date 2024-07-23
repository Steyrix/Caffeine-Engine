package engine.feature.tiled.data

data class TileMapSettings(
    var relativeWidth: Float = 0f,
    var relativeHeight: Float = 0f,
    var absoluteWidth: Float = 0f,
    var absoluteHeight: Float = 0f,
    var absoluteTileWidth: Float = 0f,
    var absoluteTileHeight: Float = 0f,
    var widthInTiles: Int = 0,
    var heightInTiles: Int = 0
) {

    fun tilesCount(): Int = widthInTiles * heightInTiles

    fun worldWidth(): Float = relativeWidth * absoluteWidth

    fun worldHeight(): Float = relativeHeight * absoluteHeight
}
