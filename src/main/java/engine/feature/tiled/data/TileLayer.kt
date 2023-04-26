package engine.feature.tiled.data

import engine.core.render.render2D.Drawable2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.shader.Shader
import engine.core.update.SetOfParameters
import engine.feature.tiled.property.Property

class TileLayer(
        val name: String,
        val widthInTiles: Int,
        val heightInTiles: Int,
        val tileIdsData: MutableList<Int>,
        internal val set: TileSet,
        private val properties: ArrayList<Property>
): Drawable2D {

    override var shader: Shader? = null
        set(value) {
            field = value
            graphicalComponent.shader = value
        }
    override val innerDrawableComponents: MutableList<Drawable2D> = mutableListOf()

    private val graphicalComponent: OpenGlObject2D = TileLayerInitializer.genGraphicalComponent(this)

    override fun updateParameters(parameters: SetOfParameters) {
        graphicalComponent.updateParameters(parameters)
    }

    override fun draw() {
        graphicalComponent.draw()
    }

    fun getTileValueByIndex(index: Int): Int {
        return when {
            index < 0 || index >= tileIdsData.size -> -1
            index <= tileIdsData.size - 1 -> tileIdsData[index]
            else -> - 1
        }
    }

    // todo: move magic numbers to constants
    fun setTileAt(index: Int, tileId: Int) {
        tileIdsData[index] = tileId

        val data = set.getTileByNumber(tileId).tileUV
        val offset = (data.size * 4 * (index)).toLong()

        graphicalComponent.updateBuffer(1, offset, data)
    }
}