package engine.feature.tiled.data

import engine.core.render.Drawable
import engine.core.render.Model
import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.property.Property

class TileLayer(
    val name: String,
    val widthInTiles: Int,
    val heightInTiles: Int,
    val tileIdsData: MutableList<Int>,
    internal val set: TileSet,
    private val properties: ArrayList<Property>
) : Drawable<SetOfStatic2DParameters> {

    override var shader: Shader? = null
        set(value) {
            field = value
            graphicalComponent.shader = value
        }

    override var zLevel: Float = 0f

    private val graphicalComponent: Model = TileLayerInitializer.genGraphicalComponent(this)

    override fun updateParameters(parameters: SetOfStatic2DParameters) {
        graphicalComponent.updateParameters(parameters)
    }

    override fun draw() {
        graphicalComponent.draw()
    }

    fun getTileValueByIndex(index: Int): Int {
        return when {
            index < 0 || index >= tileIdsData.size -> -1
            index <= tileIdsData.size - 1 -> tileIdsData[index]
            else -> -1
        }
    }

    // todo: move magic numbers to constants
    fun setTileAt(index: Int, tileId: Int) {
        tileIdsData[index] = tileId

        val data = set.getTileByNumber(tileId).tileUV
        val offset = (data.size * 4 * (index)).toLong()

        graphicalComponent.updateMesh(
            1,
            offset,
            data
        )
    }
}