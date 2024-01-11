package engine.feature.tiled.data

import engine.core.render.Model
import engine.core.geometry.Point2D
import engine.feature.tiled.traversing.TileGraph

object TileLayerInitializer {
    private const val EMPTY_TILE_ID = -1

    internal fun genGraphicalComponent(
        layer: TileLayer
    ): Model {
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

        return Model(
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
            { i: Int -> i < data.size - 1 } to { i: Int -> i + 1 }, // right tile
            { i: Int -> i + widthInTiles < data.size } to { i: Int -> i + widthInTiles }, // bottom tile
            { i: Int -> i - widthInTiles >= 0 } to { i: Int -> i - widthInTiles }, // upper tile
            { i: Int -> i > 0 } to { i: Int -> i - 1 }, // left tile
        )

        val diagonalPredicates = listOf(
            { i: Int -> i - widthInTiles - 1 >= 0 } to { i: Int -> i - widthInTiles - 1 }, // left upper tile
            { i: Int -> i - widthInTiles + 1 >= 0 } to { i: Int -> i - widthInTiles + 1 }, // right upper tile
            { i: Int -> i + widthInTiles + 1 < data.size } to { i: Int -> i + widthInTiles + 1 }, // right bottom tile
            { i: Int -> i + widthInTiles - 1 < data.size } to { i: Int -> i + widthInTiles - 1 }, // left bottom tile
        )

        val out = mutableListOf<Int>()

        predicates.forEach {
            if (it.first(num) && data[num] != EMPTY_TILE_ID) {
                val toAdd = it.second(num)
                if (data[toAdd] != EMPTY_TILE_ID) {
                    out.add(toAdd)
                }
            }
        }

        diagonalPredicates.forEachIndexed { i, it ->
            val condition = when(i) {
                0 -> { x: Int -> predicates[3].first(x) && predicates[2].first(x) }
                1 -> { x: Int -> predicates[0].first(x) && predicates[2].first(x) }
                2 -> { x: Int -> predicates[0].first(x) && predicates[1].first(x) }
                else -> { x: Int -> predicates[3].first(x) && predicates[1].first(x) }
            }

            if (it.first(num) && condition(num) && data[num] != EMPTY_TILE_ID) {
                val toAdd = it.second(num)
                if (data[toAdd] != EMPTY_TILE_ID) {
                    out.add(toAdd)
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
            set.relativeTileWidth * (pos.x + 1), set.relativeTileHeight * pos.y
        )
    }
}