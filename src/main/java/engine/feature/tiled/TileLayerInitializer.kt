package engine.feature.tiled

import engine.core.render.render2D.OpenGlObject2D
import engine.feature.geometry.Point2D
import engine.feature.tiled.traversing.TileGraph

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
            } else {
                val uvArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
                val vertices = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
                allVertices.addAll(vertices.toList())
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

    internal fun generateTileGraph(
            walkableLayers: List<TileLayer>,
            obstacleLayers: List<TileLayer> = emptyList()
    ): TileGraph? {
        val out = hashMapOf<Int, MutableList<Int>>()
        val dataLists = walkableLayers.map { it.tileIdsData }
        val widthInTiles = walkableLayers.firstOrNull()?.widthInTiles ?: return null
        val indices = walkableLayers.first().tileIdsData.indices

        // TODO find possible bug when obstacle tile is not added to the graph
        for (num in indices) {
            val adjacentTiles = dataLists
                    .map { getAdjacentTiles(it, widthInTiles, num) }
                    .flatten()
                    .distinct()
                    .toMutableList()

            out[num] = adjacentTiles
        }

        val result = TileGraph(out)

        obstacleLayers.forEach {
            it.tileIdsData.forEachIndexed { index, tileValue ->
                if (tileValue != EMPTY_TILE_ID) {
                    result.increaseCost(index)
                }
            }
        }

        return result
    }

    private fun getAdjacentTiles(
            data: List<Int>,
            widthInTiles: Int,
            num: Int
    ): MutableList<Int> {
        val predicates = listOf(
                { i: Int -> i < data.size - 1 } to { i: Int -> i + 1 },
                { i: Int -> i + widthInTiles < data.size } to { i: Int -> i + widthInTiles },
                { i: Int -> i - widthInTiles >= 0 } to { i: Int -> i - widthInTiles },
                { i: Int -> i > 0 } to { i: Int -> i - 1 },
        )

        val out = mutableListOf<Int>()

        predicates.forEach {
            if (it.first(num) && data[num] != EMPTY_TILE_ID) {
                out.add(it.second(num))
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