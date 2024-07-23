package engine.feature.tiled.data

data class TileMapSettings(
    val relativeWidth: Float,
    val relativeHeight: Float,
    var absoluteWidth: Float,
    var absoluteHeight: Float,
    var absoluteTileWidth: Float,
    var absoluteTileHeight: Float,
    var widthInTiles: Int,
    var heightInTiles: Int
) {
    var tilesCount = widthInTiles * heightInTiles
}
