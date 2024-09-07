package engine.feature.procedural.`object`

import engine.core.geometry.Point2D
import engine.feature.procedural.MapElementType
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.data.layer.ObjectsLayer

typealias PositionToTileSetIndex = Pair<Point2D, Int>

interface GeneratedTiledObject {

    val type: MapElementType
    val tileSet: TileSet

    /**
     * Should be sorted from highest to lowest
     */
    val tileIdsInSet: List<Int>

    fun List<GeneratedTiledObject>.toObjecstLayer(): ObjectsLayer {
        TODO()
    }

    /**
     * Method for determining positions' indices of object tiles on tile map
     */
    fun getObjectTilesPositions(
        startPos: Point2D,
        widthInTiles: Int,
        tileSize: Float
    ): List<PositionToTileSetIndex> {
        val result = mutableListOf<PositionToTileSetIndex>()

        var previousIndex = -1
        var previousPoint = Point2D(0f, 0f)

        tileIdsInSet.forEachIndexed { index, i ->
            if (index == 0) {
                result.add(startPos to i)

                previousIndex = i
                previousPoint = startPos
            } else {
                val verticalOffset = previousIndex / widthInTiles - i / widthInTiles

                val horizontalOffset =
                    (previousIndex % widthInTiles) - (i % widthInTiles) * tileSize

                val x = previousPoint.x + horizontalOffset
                val y = previousPoint.y + verticalOffset

                result.add(Point2D(x, y) to i)
            }
        }

        return result
    }
}