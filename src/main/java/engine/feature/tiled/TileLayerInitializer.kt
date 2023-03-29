package engine.feature.tiled

import engine.core.render.render2D.OpenGlObject2D
import engine.feature.geometry.Point2D
import engine.feature.tiled.traversing.Graph
import engine.feature.tiled.traversing.Node

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

    // TODO: graphs merging
    internal fun genGraphV2(layers: List<TileLayer>): Graph<Int> {
        val dataSets = layers.map { it.tileIdsData }

        if (layers.isEmpty()) return Graph(mutableSetOf())

        val widthInTiles = layers.first().widthInTiles

        val result = Graph<Int>(mutableSetOf())

        dataSets.forEach { data ->
            for (num in data.indices) {
                val predicates = getPredicates(data, widthInTiles)

                if (data[num] != EMPTY_TILE_ID) {
                    val node = Node(mutableSetOf(), num)

                    predicates.forEach {
                        if (it.key(num)) {
                            node.adjacencyNodes.add(it.value(num))
                        }
                    }

                    result.nodes.add(node)
                }
            }
        }

        return result
    }

    private fun getPredicates(
            data: List<Int>,
            widthInTiles: Int
    ): Map<(Int) -> Boolean, (Int) -> Int> {
        val notEmpty = { i: Int -> data[i] != EMPTY_TILE_ID }
        return mapOf(
                { i: Int -> i + 1 < data.size && notEmpty(i + 1) }
                        to { i: Int -> i + 1 },
                { i: Int -> i - 1 >= 0 && notEmpty(i - 1) }
                        to { i: Int -> i - 1 },
                { i: Int -> i + widthInTiles < data.size && notEmpty(i + widthInTiles) }
                        to { i: Int -> i + widthInTiles },
                { i: Int -> i - widthInTiles >= 0 && notEmpty(i - widthInTiles) }
                        to { i: Int -> i - widthInTiles },
                { i: Int -> i - widthInTiles - 1 >= 0 && widthInTiles > 2 && notEmpty(i - widthInTiles - 1) }
                        to { i: Int -> i - widthInTiles - 1 },
                { i: Int -> i - widthInTiles + 1 >= 0 && widthInTiles > 2 && notEmpty(i - widthInTiles + 1) }
                        to { i: Int -> i - widthInTiles + 2 },
                { i: Int -> i + widthInTiles + 1 < data.size && widthInTiles > 2 && notEmpty(i + widthInTiles + 1) }
                        to { i: Int -> i + widthInTiles + 1 },
                { i: Int -> i + widthInTiles - 1 < data.size && widthInTiles > 2 && notEmpty(i + widthInTiles - 1) }
                        to { i: Int -> i + widthInTiles - 2 },
        )
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