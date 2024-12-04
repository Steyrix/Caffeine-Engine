package engine.feature.tiled.data.layer

import engine.core.update.SetOfStaticParameters
import engine.feature.tiled.data.TileSet

interface Layer {

    val name: String

    val widthInTiles: Int

    val heightInTiles: Int

    val set: TileSet

    val tileIdsData: List<Int>

    fun updateParameters(parameters: SetOfStaticParameters)

    fun getTileValueByIndex(index: Int): Int

    fun setTileAt(index: Int, tileId: Int)
}