package engine.feature.tiled

import engine.core.entity.Entity
import engine.core.render.render2D.Drawable2D
import engine.core.shader.Shader
import engine.core.update.SetOf2DParameters
import engine.core.update.SetOfParameters
import kotlin.math.roundToInt

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

    private var absoluteHeight: Float = 0f
    private var absoluteWidth: Float = 0f

    init {
        if (layers.isEmpty()) throw IllegalStateException("Cannot initialize map with empty list of layers")

        set = layers.first().set

        layers.forEach {
            innerDrawableComponents.add(it)
        }
    }

    fun getTileWidth() = set.relativeTileWidth

    fun getTileHeight() = set.relativeTileHeight

    fun getLayerByName(name: String): TileLayer = layersMap[name]
            ?: throw IllegalStateException("Layer with name $name not found")

    fun getTileIndexInLayer(posX: Float, posY: Float, layerName: String): Int {
        val layer = layersMap[layerName] ?: return NOT_FOUND
        val widthInTiles = layer.widthInTiles

        val currentTileWidth = absoluteWidth / layer.widthInTiles
        val currentTileHeight = absoluteHeight / layer.widthInTiles

        val xTileNumber = getTilePosition(currentTileWidth, posX)
        val yTileNumber = getTilePosition(currentTileHeight, posY)
        return yTileNumber * widthInTiles + xTileNumber
    }

    private fun getTilePosition(tileSize: Float, pos: Float): Int {
        val roundedPos = pos.roundToInt()
        if (roundedPos == 0) {
            return 0
        }

        return roundedPos / tileSize.roundToInt()
    }

    override fun updateParameters(parameters: SetOfParameters) {
        if (parameters is SetOf2DParameters) {
            absoluteWidth = parameters.xSize
            absoluteHeight = parameters.ySize
        }

        innerDrawableComponents.forEach {
            it.updateParameters(parameters)
        }
    }
}