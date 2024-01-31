package engine.feature.tiled.data.layer

import engine.core.render.Model
import engine.core.geometry.Point2D
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.traversing.TileGraph
import org.lwjgl.opengl.GL33C

object TileLayerInitializer {

    private const val EMPTY_TILE_ID = -1

    internal fun genLayerObjects(
        widthInTiles: Int,
        data: List<Int>,
        set: TileSet,
        transparencyUniform: String
    ): List<LayerObject> {
        val out = mutableListOf<LayerObject>()
        val addedTiles = mutableSetOf<Int>()

        for (num in data.indices) {
            if (!addedTiles.contains(num) && data[num] != EMPTY_TILE_ID) {
                addedTiles.add(num)
                val objectIndices = getObjectTiles(
                    widthInTiles, data, num, addedTiles
                )

                val objectVertices = mutableListOf<Float>()
                val objectUv = mutableListOf<Float>()

                objectIndices.forEach {
                    val value = data[it]
                    val pos = getPositionByTileIndex(it, widthInTiles)
                    objectUv.addAll(set.getTileByNumber(value).tileUV.toList())
                    objectVertices.addAll(genVertices(pos, set).toList())
                }

                val model = Model(
                    dataArrays = listOf(
                        objectVertices.toFloatArray(),
                        objectUv.toFloatArray()
                    ),
                    verticesCount = objectVertices.size / 2,
                    texture = set.texture2D
                ).apply {
                    zLevel = 2f
                }

                out.add(
                    LayerObject(
                        objectIndices.toSet(),
                        model,
                        transparencyUniform
                    )
                )
            }
        }

        return out
    }

    private fun getObjectTiles(
        width: Int,
        data: List<Int>,
        initialTile: Int,
        addedTiles: MutableSet<Int>
    ): List<Int> {
        val indices = mutableListOf(initialTile)
        val indicesToCheck = ArrayDeque<Int>()
        indicesToCheck.add(initialTile)

        while (indicesToCheck.isNotEmpty()) {
            val num = indicesToCheck.removeFirst()
            val adjacentTiles = getAdjacentTiles(
                data,
                width,
                num
            )

            val toAdd = mutableListOf<Int>()
            adjacentTiles.forEach {
                if (!addedTiles.contains(it)) {
                    toAdd.add(it)
                    indicesToCheck.add(it)
                }
            }

            indices.addAll(toAdd)
            addedTiles.addAll(toAdd)
        }

        return indices
    }

    internal fun genLayerModel(
        data: List<Int>,
        set: TileSet,
        widthInTiles: Int
    ): Model {
        val allVertices: ArrayList<Float> = ArrayList()
        val allUV: ArrayList<Float> = ArrayList()

        for (num in data.indices) {
            val pos = getPositionByTileIndex(num, widthInTiles)
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
        ).apply { zLevel = 1f }
    }

    internal fun genDebugGraphicalComponent(
        data: List<Int>,
        set: TileSet,
        widthInTiles: Int
    ): Model {
        val allVertices: ArrayList<Float> = ArrayList()
        for (num in data.indices) {
            val pos = getPositionByTileIndex(num, widthInTiles)
            val tileNumber = data[num]

            val verticesArray = genDebugVertices(pos, set)

            if (tileNumber != EMPTY_TILE_ID) {
                allVertices.addAll(verticesArray.toList())
            } else {
                val vertices = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
                allVertices.addAll(vertices.toList())
            }
        }

        return Model(
            dataArrays = listOf(allVertices.toFloatArray()),
            verticesCount = allVertices.size / 2
        ).apply {
            drawMode = GL33C.GL_LINES
            zLevel = 2f
        }
    }

    internal fun generateTileGraph(
        walkableLayers: List<Layer>,
        obstacleLayers: List<Layer> = emptyList()
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
            val condition = when (i) {
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

    private fun genDebugVertices(pos: Point2D, set: TileSet): FloatArray {
        return floatArrayOf(
            set.relativeTileWidth * pos.x, set.relativeTileHeight * pos.y,
            set.relativeTileWidth * (pos.x + 1), set.relativeTileHeight * pos.y,
            set.relativeTileWidth * (pos.x + 1), set.relativeTileHeight * pos.y,
            set.relativeTileWidth * (pos.x + 1), set.relativeTileHeight * (pos.y + 1),
            set.relativeTileWidth * (pos.x + 1), set.relativeTileHeight * (pos.y + 1),
            set.relativeTileWidth * pos.x, set.relativeTileHeight * (pos.y + 1),
            set.relativeTileWidth * pos.x, set.relativeTileHeight * (pos.y + 1),
            set.relativeTileWidth * pos.x, set.relativeTileHeight * pos.y
        )
    }
}