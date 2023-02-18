package engine.feature.tiled

import engine.core.render.render2D.Drawable2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.shader.Shader
import engine.core.update.SetOfParameters
import engine.feature.geometry.Point2D
import engine.feature.tiled.property.Property

// TODO: implement graph representation of a tile layer
class TileLayer(
        val name: String,
        val widthInTiles: Int,
        val heightInTiles: Int,
        val tileIdsData: List<Int>,
        internal val set: TileSet,
        private val properties: ArrayList<Property>
): Drawable2D {

    companion object {
        private const val EMPTY_TILE_ID = -1

        private fun genGraphicalComponent(
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

    override var shader: Shader? = null
        set(value) {
            field = value
            graphicalComponent.shader = value
        }
    override val innerDrawableComponents: MutableList<Drawable2D> = mutableListOf()

    private var initialWidth = 0f
    private var initialHeight = 0f
    private val graphicalComponent: OpenGlObject2D

    init {
        initialWidth = (widthInTiles * set.tileWidthPx)
        initialHeight = (heightInTiles * set.tileHeightPx)
        graphicalComponent = genGraphicalComponent(this)
    }

    override fun updateParameters(parameters: SetOfParameters) {
        graphicalComponent.updateParameters(parameters)
    }

    override fun draw() {
        graphicalComponent.draw()
    }

    fun getTileNumberByIndex(index: Int): Int {
        return if (index <= tileIdsData.size - 1) tileIdsData[index]
        else -1
    }
}