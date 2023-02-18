package engine.feature.tiled

import engine.core.render.render2D.OpenGlObject2D
import engine.feature.geometry.Point2D

object TileUtil {
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

            if (num != data.size - 1) { // if not last
                out[num]?.add(num + 1)
            }

            if (num != 0) { // if not first
                out[num]?.add(num - 1)
            }

            if (num + layer.widthInTiles < data.size) { // if there is something below
                out[num]?.add(num + layer.widthInTiles)
            }

            if (num - layer.widthInTiles >= 0) { // if there is something above
                out[num]?.add(num - layer.widthInTiles)
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