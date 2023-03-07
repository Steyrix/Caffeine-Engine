package engine.feature.tiled.traversing

import engine.feature.tiled.TileLayer

object TileGraphGenerator {
    private const val EMPTY_TILE_ID = -1

    internal fun generateTileGraph(layer: TileLayer): HashMap<Int, MutableList<Int>> {
        val out = hashMapOf<Int, MutableList<Int>>()
        val data = layer.tileIdsData

        for (num in data.indices) {
            out[num] = mutableListOf()
            if (data[num] != EMPTY_TILE_ID) {
                if (num != data.size - 1) {
                    val target = num + 1
                    if (data[target] != EMPTY_TILE_ID) out[num]?.add(target)
                }

                if (num != 0) {
                    val target = num - 1
                    if (data[target] != EMPTY_TILE_ID) out[num]?.add(target)
                }

                if (num + layer.widthInTiles < data.size) {
                    val target = num + layer.widthInTiles
                    if (data[target] != EMPTY_TILE_ID) out[num]?.add(target)
                }

                if (num - layer.widthInTiles >= 0) {
                    val target = num - layer.widthInTiles
                    if (data[target] != EMPTY_TILE_ID) out[num]?.add(target)
                }
            }
        }

        return out
    }
}