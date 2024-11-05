package engine.feature.tiled.data

import engine.core.entity.Entity
import engine.core.render.Drawable
import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters
import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.feature.tiled.data.layer.*
import engine.feature.tiled.scene.TileSelectionData
import engine.feature.tiled.traversing.TileGraph
import kotlin.math.roundToInt

// TODO: parameters should be set from outside
// TODO: get rid of retrieving shared parameters from layers and tilesets
class TileMap(
    val layers: List<Layer>,
    widthInTiles: Int = 0,
    heightInTiles: Int = 0
) : Drawable<SetOfStatic2DParameters>, Entity {

    companion object {
        private const val NOT_FOUND = -1
    }

    private val set: TileSet
    private val tileSets: MutableList<TileSet> = mutableListOf()
    private val layersMap = layers.associateBy { it.name }
    private val settings: TileMapSettings = TileMapSettings()

    var shaders: TileMapShaders = TileMapShaders()
        set(value) {
            layers.forEach {
                if (it is ObjectsLayer) it.objectShaderCreator = value.objectShaderCreator
                if (it is TileLayer) {
                    it.debugShader = value.debugShader
                    it.shader = value.mainShader
                }
            }
            this.shader = value.mainShader
            field = value
        }

    val tilesCount: Int
        get() = settings.tilesCount()
    var graph: TileGraph? = null

    override var zLevel: Float = 0f
    override var shader: Shader? = null

    var isDebugMeshEnabled: Boolean = false
        set(value) {
            field = value
            layers.forEach {
                if (it is TileLayer) {
                    it.isDebugMeshEnabled = value
                }
            }
        }

    init {
        if (layers.isEmpty()) throw IllegalStateException("Cannot initialize map with empty list of layers")

        set = layers.first().set
        layers.forEach {
            checkSetParameters(it)
            tileSets.add(it.set)
        }

        settings.widthInTiles = widthInTiles
        settings.heightInTiles = heightInTiles
        settings.relativeHeight = settings.heightInTiles * set.relativeTileHeight
        settings.relativeWidth = settings.widthInTiles * set.relativeTileWidth
    }

    private fun checkSetParameters(layer: Layer) {
        val relativeWidth = layer.set.relativeTileWidth
        val relativeHeight = layer.set.relativeTileHeight

        if (
            layer.set.relativeTileWidth != relativeWidth
            || layer.set.relativeTileHeight != relativeHeight
        ) {
            throw IllegalStateException("Tile sets dimension are not equal")
        }
    }

    fun getTileHeight() = settings.absoluteTileHeight

    fun getTileWidth() = settings.absoluteTileWidth

    // TODO: generate procedurally, retrieve by type, not by name
    fun getLayerByName(name: String): Layer? = layersMap[name]

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
        val rowIndex = index / settings.widthInTiles
        val columnIndex = index - rowIndex * settings.widthInTiles

        val x = columnIndex * settings.absoluteWidth * set.relativeTileWidth
        val y = rowIndex * settings.absoluteHeight * set.relativeTileHeight

        return Point2D(x, y)
    }

    fun getTileIndex(posX: Float, posY: Float): Int {
        val xTileNumber = getTileAlignmentInMap(settings.absoluteTileWidth, posX)
        val yTileNumber = getTileAlignmentInMap(settings.absoluteTileHeight, posY)

        if (xTileNumber < 0 || yTileNumber < 0) return -1

        return yTileNumber * settings.widthInTiles + xTileNumber
    }

    fun generateGraph(
        walkableLayers: List<String>,
        obstacleLayers: List<String>
    ) {
        val walkable = walkableLayers.mapNotNull { getLayerByName(it) }
        val obstacle = obstacleLayers.mapNotNull { getLayerByName(it) }
        graph = TileLayerInitializer.generateTileGraph(walkable, obstacle)
    }

    fun setTileAt(layerName: String, posX: Float, posY: Float, tileId: Int) {
        val layer = getLayerByName(layerName)
        layer?.setTileAt(getTileIndex(posX, posY), tileId)
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
        settings.update(parameters)

        layers.forEach {
            it.updateParameters(parameters)
        }
    }

    override fun draw() {
        layers.forEach {
            when (it) {
                is TileLayer -> it.draw()
            }
        }
    }

    fun getWorldWidth() = settings.worldWidth()

    fun getWorldHeight() = settings.worldHeight()

    fun absoluteTileWidth() = settings.absoluteTileWidth

    fun absoluteTileHeight() = settings.absoluteTileHeight

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
        return TileLayerInitializer.genVerticesBuffer(
            layers[0].tileIdsData,
            set,
            settings.widthInTiles
        )
    }

    fun getNetForTileSelection(
        tileSelectionData: TileSelectionData,
        shader: Shader
    ): Model {
        return TileLayerInitializer.getNetForTileSelection(
            tileSelectionData,
            shader
        ).apply {
            isPartOfWorldTranslation = false
            zLevel = 2f
        }
    }
}