package engine.feature.tiled.data

import engine.core.update.SetOfStatic2DParameters

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

    fun worldWidth(): Float = absoluteWidth

    fun worldHeight(): Float = absoluteHeight

    fun update(parameters: SetOfStatic2DParameters) {
        absoluteWidth = parameters.xSize
        absoluteHeight = parameters.ySize
        absoluteTileWidth = absoluteWidth / widthInTiles
        absoluteTileHeight = absoluteHeight / heightInTiles
    }
}
