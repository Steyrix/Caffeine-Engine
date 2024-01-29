package engine.feature.tiled.data.layer

import engine.core.update.SetOfStatic2DParameters

interface Layer {
    fun updateParameters(parameters: SetOfStatic2DParameters)

    fun getTileValueByIndex(index: Int): Int

    fun setTileAt(index: Int, tileId: Int)
}