package engine.feature.tiled.data.layer

import engine.core.entity.CompositeEntity
import engine.core.render.model.Model
import engine.core.render.shader.Shader
import engine.core.update.SetOfStaticParameters
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.property.Property

// TODO: condense
class TileLayer(
    override val name: String,
    override val widthInTiles: Int,
    override val heightInTiles: Int,
    override val set: TileSet,
    override val tileIdsData: MutableList<Int>,
    private val properties: MutableList<Property> = mutableListOf(),
    model: Model?
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

    var isDebugMeshEnabled: Boolean = false
    set(value) {
        if (value) {
            addComponent(debugGraphicalComponent, paramsKey)
        } else {
            removeComponent(debugGraphicalComponent)
        }
        field = value
    }

    private val graphicalComponent: Model = model ?: TileLayerInitializer.genLayerModel(
        tileIdsData, set, widthInTiles
    )

    // TODO: cover by debug flag
    private val debugGraphicalComponent: Model = TileLayerInitializer.genDebugGraphicalComponent(
        tileIdsData, set, widthInTiles
    )

    private val paramsKey = SetOfStaticParameters()

    init {
        addComponent(graphicalComponent, paramsKey)
    }

    override fun updateParameters(parameters: SetOfStaticParameters) {
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