package engine.feature.tiled.data.`object`

import engine.core.game_object.SingleGameEntity
import engine.core.game_object.SpawnOptions
import engine.feature.tiled.data.layer.LayerObject

class MapObjectEntity(
    private val layerObject: LayerObject
) : SingleGameEntity() {

    var level = 0f

    init {
        it = layerObject
    }

    override fun getZLevel(): Float {
        return level
    }

    override var isSpawned: Boolean = true
    override fun preSpawn(spawnOptions: SpawnOptions) = Unit
}