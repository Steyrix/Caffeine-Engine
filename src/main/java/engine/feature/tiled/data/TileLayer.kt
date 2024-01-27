package engine.feature.tiled.data

import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.shader.Shader
import engine.core.update.ParametersFactory
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.property.Property

class TileLayer(
    val name: String,
    val widthInTiles: Int,
    val heightInTiles: Int,
    val tileIdsData: MutableList<Int>,
    internal val set: TileSet,
    private val properties: ArrayList<Property>
) : CompositeEntity() {

    var shader: Shader? = null
        set(value) {
            field = value
            graphicalComponent.shader = value
        }

    var debugShader: Shader? = null
        set(value) {
            field = value
            debugGraphicalComponent.shader = value
        }

    private val graphicalComponent: Model = TileLayerInitializer.genGraphicalComponent(this)

    // TODO: cover by debug flag
    private val debugGraphicalComponent: Model = TileLayerInitializer.genDebugGraphicalComponent(this)

    private val paramsKey = ParametersFactory.createEmptyStatic()

    val dynamicObjects = mutableListOf<LayerObject>()

    init {
        addComponent(graphicalComponent, paramsKey)
        addComponent(debugGraphicalComponent, paramsKey)
    }

    fun updateParameters(parameters: SetOfStatic2DParameters) {
        graphicalComponent.updateParameters(parameters)
        debugGraphicalComponent.updateParameters(parameters)
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