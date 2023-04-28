package engine.feature.tiled.data

import engine.core.entity.Entity
import engine.core.render.render2D.Drawable2D
import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters
import engine.core.update.SetOfParameters
import engine.feature.geometry.Point2D
import engine.feature.tiled.traversing.TileGraph
import kotlin.math.roundToInt

// TODO: remove doc for properties and rename them for clearance
class TileMap(
        layers: MutableList<TileLayer>,
) : Drawable2D, Entity {

    companion object {
        private const val NOT_FOUND = -1
    }

    override var shader: Shader? = null
        set(value) {
            field = value
            innerDrawableComponents.forEach {
                it.shader = value
            }
        }

    override val innerDrawableComponents: MutableList<Drawable2D> = mutableListOf()

    private val set: TileSet
    private val layersMap = layers.associateBy { it.name }

    /*
        Represents the size of map relative to the screen size
     */
    val relativeMapHeight: Float
    val relativeMapWidth: Float

    /*
        Represents the absolute size of map in pixels
     */
    private var absoluteMapHeight: Float = 0f
    private var absoluteMapWidth: Float = 0f

    /*
        Represents the absolute size of tile in pixels
     */
    private var absoluteTileWidth: Float = 0f
    private var absoluteTileHeight: Float = 0f

    /*
        Represents the number of rows and columns of tiles in the map
     */
    private var widthInTiles: Int = 0
    private var heightInTiles: Int = 0

    val tilesCount: Int
        get() = widthInTiles * heightInTiles

    init {
        if (layers.isEmpty()) throw IllegalStateException("Cannot initialize map with empty list of layers")

        set = layers.first().set

        widthInTiles = layers.first().widthInTiles
        heightInTiles = layers.first().heightInTiles

        relativeMapHeight = heightInTiles * set.relativeTileHeight
        relativeMapWidth = widthInTiles * set.relativeTileWidth

        layers.forEach {
            innerDrawableComponents.add(it)
        }
    }

    fun getTileHeight() = absoluteTileHeight

    fun getTileWidth() = absoluteTileWidth

    fun getLayerByName(name: String): TileLayer = layersMap[name]
            ?: throw IllegalStateException("Layer with name $name not found")

    fun getTileValue(posX: Float, posY: Float, layerName: String): Int {
        val layer = layersMap[layerName] ?: return NOT_FOUND
        val index = getTileIndex(posX, posY)
        return layer.getTileValueByIndex(index)
    }

    fun getTilePosition(index: Int): Point2D {
        val rowIndex = index / widthInTiles
        val columnIndex = index - rowIndex * widthInTiles

        val x = columnIndex * absoluteMapWidth * set.relativeTileWidth
        val y = rowIndex * absoluteMapHeight * set.relativeTileHeight

        return Point2D(x, y)
    }

    fun getTileIndex(posX: Float, posY: Float): Int {
        val xTileNumber = getTileAlignmentInMap(absoluteTileWidth, posX)
        val yTileNumber = getTileAlignmentInMap(absoluteTileHeight, posY)

        if (xTileNumber < 0 || yTileNumber < 0) return -1

        return yTileNumber * widthInTiles + xTileNumber
    }

    fun getGraph(
            walkableLayers: List<String>,
            obstacleLayers: List<String>
    ): TileGraph? {
        val walkable = walkableLayers.map { getLayerByName(it) }
        val obstacle = obstacleLayers.map { getLayerByName(it) }
        return TileLayerInitializer.generateTileGraph(walkable, obstacle)
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

    override fun updateParameters(parameters: SetOfParameters) {
        if (parameters is SetOfStatic2DParameters) {
            absoluteMapWidth = parameters.xSize
            absoluteMapHeight = parameters.ySize
            absoluteTileWidth = relativeMapWidth / widthInTiles * absoluteMapWidth
            absoluteTileHeight = relativeMapHeight / heightInTiles * absoluteMapHeight
        }

        innerDrawableComponents.forEach {
            it.updateParameters(parameters)
        }
    }
}