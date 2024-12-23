package engine.feature.tiled.data.layer

import engine.core.render.model.Model
import engine.core.geometry.Point2D
import engine.core.render.shader.Shader
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.scene.TileSelectionData
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
                    objectVertices.addAll(genVertices(pos, set, widthInTiles).toList())
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
        addedIndices: MutableSet<Int>
    ): List<Int> {
        val indices = mutableListOf(initialTile)
        val indicesToCheck = ArrayDeque<Int>()
        indicesToCheck.add(initialTile)

        while (indicesToCheck.isNotEmpty()) {
            val num = indicesToCheck.removeFirst()
            val adjacentIndices = getAdjacentTiles(
                data,
                width,
                num,
                shouldIncludeDiagonals = false
            )

            val toAdd = mutableListOf<Int>()
            adjacentIndices.forEach {
                if (!addedIndices.contains(it)) {
                    toAdd.add(it)
                    indicesToCheck.add(it)
                }
            }

            indices.addAll(toAdd)
            addedIndices.addAll(toAdd)
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
            val verticesArray = genVertices(
                pos,
                set,
                widthInTiles
            )

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

            val verticesArray = genDebugVertices(pos, set, widthInTiles)

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

    internal fun genVerticesBuffer(
        data: List<Int>,
        set: TileSet,
        widthInTiles: Int
    ): MutableList<Float> {
        val allVertices: ArrayList<Float> = ArrayList()

        for (num in data.indices) {
            val pos = getPositionByTileIndex(num, widthInTiles)
            val verticesArray = genVertices(pos, set, widthInTiles)
            allVertices.addAll(verticesArray.toList())
        }

        return allVertices
    }

    internal fun generateTileGraph(
        walkableLayers: List<Layer> = emptyList(),
        obstacleLayers: List<Layer> = emptyList()
    ): TileGraph? {
        if (walkableLayers.isEmpty()) return null
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
        num: Int,
        shouldIncludeDiagonals: Boolean = true
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

        if (shouldIncludeDiagonals) {
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
        }

        return out
    }

    private fun getPositionByTileIndex(num: Int, widthInTiles: Int): Point2D {
        val x: Int = num % widthInTiles
        val y: Int = num / widthInTiles

        return Point2D(x.toFloat(), y.toFloat())
    }

    private fun genVertices(
        pos: Point2D,
        set: TileSet,
        widthInTiles: Int
    ): FloatArray {
        val width = set.relativeTileWidth / (widthInTiles * set.relativeTileWidth)
        val height = set.relativeTileHeight / (widthInTiles * set.relativeTileHeight)
        return floatArrayOf(
            width * pos.x, height * (pos.y + 1),
            width * (pos.x + 1), height * pos.y,
            width * pos.x, height * pos.y,
            width * pos.x, height * (pos.y + 1),
            width * (pos.x + 1), height * (pos.y + 1),
            width * (pos.x + 1), height * pos.y
        )
    }

    fun getNetForTileSelection(
        tileSelectionData: TileSelectionData,
        shader: Shader
    ): Model {
        val allVertices = mutableListOf<Float>()

        var currentRow = 0
        var currentColumn = 0
        var counter = 0

        while (counter != tileSelectionData.width * tileSelectionData.height) {
            if (currentColumn >= tileSelectionData.width) {
                currentRow++
                currentColumn = 0
            }
            val pos = Point2D(
                currentColumn.toFloat(),
                currentRow.toFloat()
            )

            currentColumn++

            val vertices = genSelectionNetVertices(pos, tileSelectionData)
            allVertices.addAll(vertices.toList())
            counter++
        }

        return Model(
            dataArrays = listOf(allVertices.toFloatArray()),
            verticesCount = allVertices.size / 2
        ).apply {
            this.shader = shader
            drawMode = GL33C.GL_LINES
            zLevel = 2f
        }
    }

    private fun genDebugVertices(
        pos: Point2D,
        set: TileSet,
        widthInTiles: Int
    ): FloatArray {
        val width = set.tileWidthPx / (widthInTiles * set.tileHeightPx)
        val height = set.tileWidthPx / (widthInTiles * set.tileHeightPx)
        return floatArrayOf(
            width * pos.x, height * pos.y,
            width * (pos.x + 1), height * pos.y,
            width * (pos.x + 1), height * pos.y,
            width * (pos.x + 1), height * (pos.y + 1),
            width * (pos.x + 1), height * (pos.y + 1),
            width * pos.x, height * (pos.y + 1),
            width * pos.x, height * (pos.y + 1),
            width * pos.x, height * pos.y
        )
    }

    private fun genSelectionNetVertices(pos: Point2D, data: TileSelectionData): FloatArray {
        return floatArrayOf(
            pos.x / data.width, pos.y / data.height,
            (pos.x + 1) / data.width, pos.y / data.height,
            (pos.x + 1) / data.width, pos.y / data.height,
            (pos.x + 1) / data.width, (pos.y + 1) / data.height,
            (pos.x + 1) / data.width, (pos.y + 1) / data.height,
            pos.x / data.width, (pos.y + 1) / data.height,
            pos.x / data.width, (pos.y + 1) / data.height,
            pos.x / data.width, pos.y / data.height
        )
    }
}