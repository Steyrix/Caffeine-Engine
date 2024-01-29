package engine.feature.tiled.data.layer

import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileSet

interface Layer {

    val name: String

    val set: TileSet

    val widthInTiles: Int

    val heightInTiles: Int

    fun updateParameters(parameters: SetOfStatic2DParameters)

    fun getTileValueByIndex(index: Int): Int

    fun setTileAt(index: Int, tileId: Int)
}