package engine.feature.tiled.data.layer

import engine.core.entity.CompositeEntity
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.property.Property

class ObjectsLayer(
    val name: String,
    val widthInTiles: Int,
    val heightInTiles: Int,
    val tileIdsData: MutableList<Int>,
    internal val set: TileSet,
    protected val properties: ArrayList<Property>
): CompositeEntity() {



}