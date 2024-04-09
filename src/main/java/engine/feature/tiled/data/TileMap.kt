package engine.feature.tiled.data

import engine.core.entity.Entity
import engine.core.render.Drawable
import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters
import engine.core.geometry.Point2D
import engine.feature.tiled.data.layer.*
import engine.feature.tiled.traversing.TileGraph
import kotlin.math.roundToInt

// TODO: remove doc for properties and rename them for clearance
// TODO: make composite entity
class TileMap(
    private val layers: MutableList<Layer>,
) : Drawable<SetOfStatic2DParameters>, Entity {

    companion object {
        private const val NOT_FOUND = -1
    }

    override var shader: Shader? = null
        set(value) {
            field = value
            layers.forEach {
                if (it is TileLayer) it.shader = value
            }
        }

    var objectShaderCreator: () -> Shader? = {
        null
    }
        set(value) {
            field = value
            layers.forEach {
                if (it is ObjectsLayer) it.objectShaderCreator = value
            }
        }

    var debugShader: Shader? = null
        set(value) {
            field = value
            layers.forEach {
                if (it is TileLayer) it.debugShader = value
            }
        }

    override var zLevel: Float = 0f

    private val set: TileSet
    private val layersMap = layers.associateBy { it.name }

    /*
        Represents the size of map relative to the screen size
     */
    val relativeHeight: Float
    val relativeWidth: Float

    /*
        Represents the absolute size of map in pixels
     */
    private var absoluteHeight: Float = 0f
    private var absoluteWidth: Float = 0f

    /*
        Represents the absolute size of tile in pixels
     */
    var absoluteTileWidth: Float = 0f
    var absoluteTileHeight: Float = 0f

    /*
        Represents the number of rows and columns of tiles in the map
     */
    private var widthInTiles: Int = 0
    private var heightInTiles: Int = 0

    val tilesCount: Int
        get() = widthInTiles * heightInTiles

    var graph: TileGraph? = null

    init {
        if (layers.isEmpty()) throw IllegalStateException("Cannot initialize map with empty list of layers")

        set = layers.first().set

        widthInTiles = layers.first().widthInTiles
        heightInTiles = layers.first().heightInTiles

        relativeHeight = heightInTiles * set.relativeTileHeight
        relativeWidth = widthInTiles * set.relativeTileWidth
    }

    fun getTileHeight() = absoluteTileHeight

    fun getTileWidth() = absoluteTileWidth

    fun getLayerByName(name: String): Layer = layersMap[name]
        ?: throw IllegalStateException("Layer with name $name not found")

    fun getTileValue(posX: Float, posY: Float, layerName: String): Int {
        val layer = layersMap[layerName] ?: return NOT_FOUND
        val index = getTileIndex(posX, posY)
        return layer.getTileValueByIndex(index)
    }

    fun getTileValue(index: Int, layerName: String): Int {
        val layer = layersMap[layerName] ?: return NOT_FOUND
        return layer.getTileValueByIndex(index)
    }

    fun getTilePosition(index: Int): Point2D {
        val rowIndex = index / widthInTiles
        val columnIndex = index - rowIndex * widthInTiles

        val x = columnIndex * absoluteWidth * set.relativeTileWidth
        val y = rowIndex * absoluteHeight * set.relativeTileHeight

        return Point2D(x, y)
    }

    fun getTileIndex(posX: Float, posY: Float): Int {
        val xTileNumber = getTileAlignmentInMap(absoluteTileWidth, posX)
        val yTileNumber = getTileAlignmentInMap(absoluteTileHeight, posY)

        if (xTileNumber < 0 || yTileNumber < 0) return -1

        return yTileNumber * widthInTiles + xTileNumber
    }

    fun generateGraph(
        walkableLayers: List<String>,
        obstacleLayers: List<String>
    ) {
        val walkable = walkableLayers.map { getLayerByName(it) }
        val obstacle = obstacleLayers.map { getLayerByName(it) }
        graph = TileLayerInitializer.generateTileGraph(walkable, obstacle)
    }

    fun setTileAt(layerName: String, posX: Float, posY: Float, tileId: Int) {
        val layer = getLayerByName(layerName)
        layer.setTileAt(getTileIndex(posX, posY), tileId)
    }

    private fun getTileAlignmentInMap(tileSize: Float, pos: Float): Int {
        val roundedPos = pos.roundToInt()
        val roundedTileSize = tileSize.roundToInt()
        if (roundedPos == 0 || roundedTileSize == 0) {
            return 0
        }

        return roundedPos / roundedTileSize
    }

    override fun updateParameters(parameters: SetOfStatic2DParameters) {
        val initial = parameters.xSize

        parameters.xSize /= set.tilesetWidthHeightRatio
        absoluteWidth = parameters.xSize
        absoluteHeight = parameters.ySize
        absoluteTileWidth = relativeWidth / widthInTiles * absoluteWidth
        absoluteTileHeight = relativeHeight / heightInTiles * absoluteHeight

        layers.forEach {
            it.updateParameters(parameters)
        }

        parameters.xSize = initial
    }

    override fun draw() {
        layers.forEach {
            when (it) {
                is TileLayer -> it.draw()
            }
        }
    }

    fun getWorldWidth(): Float {
        return relativeWidth * absoluteWidth
    }

    fun getWorldHeight(): Float {
        return relativeHeight * absoluteHeight
    }

    fun processIntersectionIfNeeded(point: Point2D) {
        val index = getTileIndex(point.x, point.y)
        layers.filterIsInstance<ObjectsLayer>().forEach {
            if (getTileValue(index, it.name) >= 0) {
                it.processIntersection(index)
            }
        }
    }

    fun retrieveObjects(): List<LayerObject> {
        val out = mutableListOf<LayerObject>()
        layers.forEach {
            if (it is ObjectsLayer) {
                out.addAll(it.objects)
            }
        }
        return out
    }

    fun getVertices(): MutableList<Float> {
        return TileLayerInitializer.genVerticesBuffer(layers[0].tileIdsData, set, widthInTiles)
    }
}