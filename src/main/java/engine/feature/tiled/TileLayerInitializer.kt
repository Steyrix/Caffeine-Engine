package engine.feature.tiled

import engine.core.render.render2D.OpenGlObject2D
import engine.feature.geometry.Point2D

object TileLayerInitializer {
    private const val EMPTY_TILE_ID = -1

    internal fun genGraphicalComponent(
            layer: TileLayer
    ): OpenGlObject2D {
        val allVertices: ArrayList<Float> = ArrayList()
        val allUV: ArrayList<Float> = ArrayList()
        val data = layer.tileIdsData
        val set = layer.set

        for (num in data.indices) {
            val pos = getPositionByTileIndex(num, layer.widthInTiles)
            val verticesArray = genVertices(pos, set)

            val tileNumber = data[num]

            if (tileNumber != EMPTY_TILE_ID) {
                val uvArray = set.getTileByNumber(tileNumber).tileUV
                allVertices.addAll(verticesArray.toList())
                allUV.addAll(uvArray.toList())
            }
        }

        return OpenGlObject2D(
                bufferParamsCount = 2,
                dataArrays = listOf(allVertices.toFloatArray(), allUV.toFloatArray()),
                verticesCount = allVertices.size / 2,
                texture = set.texture2D
        )
    }

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

    private fun getPositionByTileIndex(num: Int, widthInTiles: Int): Point2D {
        val x: Int = num % widthInTiles
        val y: Int = num / widthInTiles

        return Point2D(x.toFloat(), y.toFloat())
    }

    private fun genVertices(pos: Point2D, set: TileSet): FloatArray {
        return floatArrayOf(
                set.relativeTileWidth * pos.x, set.relativeTileHeight * (pos.y + 1),
                set.relativeTileWidth * (pos.x + 1), set.relativeTileHeight * pos.y,
                set.relativeTileWidth * pos.x, set.relativeTileHeight * pos.y,
                set.relativeTileWidth * pos.x, set.relativeTileHeight * (pos.y + 1),
                set.relativeTileWidth * (pos.x + 1), set.relativeTileHeight * (pos.y + 1),
                set.relativeTileWidth * (pos.x + 1), set.relativeTileHeight * pos.y)
    }
}