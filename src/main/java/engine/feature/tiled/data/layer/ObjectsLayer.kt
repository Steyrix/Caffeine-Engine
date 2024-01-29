package engine.feature.tiled.data.layer

import engine.core.entity.CompositeEntity
import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileSet

class ObjectsLayer(
    override val name: String,
    override val widthInTiles: Int,
    override val heightInTiles: Int,
    override val set: TileSet,
    private val tileIdsData: MutableList<Int>,
    transparencyUniformName: String
): CompositeEntity(), Layer {

    var shader: Shader? = null

    private val objects = mutableListOf<LayerObject>()

    init {
        objects.addAll(TileLayerInitializer.genLayerObjects(
            widthInTiles, tileIdsData, set, transparencyUniformName
        ))
    }

    override fun updateParameters(parameters: SetOfStatic2DParameters) {
        objects.forEach {
            it.updateParameters(parameters)
        }
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
        val target = objects.find {
            it.tileIndices.contains(index)
        }

        target?.let {
            tileIdsData[index] = tileId
            val data = set.getTileByNumber(tileId).tileUV
            val offset = (data.size * 4 * (index)).toLong()
            it.updateMesh(1, offset, data)
        }
    }
}