package engine.feature.tiled.data.layer

import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.shader.Shader
import engine.core.update.ParametersFactory
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.property.Property

class TileLayer(
    override val name: String,
    override val widthInTiles: Int,
    override val heightInTiles: Int,
    val tileIdsData: MutableList<Int>,
    override val set: TileSet,
    private val properties: ArrayList<Property>
) : CompositeEntity(), Layer {

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

    private val graphicalComponent: Model = TileLayerInitializer.genLayerModel(
        tileIdsData, set, widthInTiles
    )

    // TODO: cover by debug flag
    private val debugGraphicalComponent: Model = TileLayerInitializer.genDebugGraphicalComponent(
        tileIdsData, set, widthInTiles
    )

    private val paramsKey = ParametersFactory.createEmptyStatic()

    init {
        addComponent(graphicalComponent, paramsKey)
        addComponent(debugGraphicalComponent, paramsKey)
    }

    override fun updateParameters(parameters: SetOfStatic2DParameters) {
        graphicalComponent.updateParameters(parameters)
        debugGraphicalComponent.updateParameters(parameters)
    }

    override fun getTileValueByIndex(index: Int): Int {
        return when {
            index < 0 || index >= tileIdsData.size -> -1
            index <= tileIdsData.size - 1 -> tileIdsData[index]
            else -> -1
        }
    }

    // todo: move magic numbers to constants
    override fun setTileAt(index: Int, tileId: Int) {
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