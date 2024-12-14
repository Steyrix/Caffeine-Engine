package engine.feature.tiled.scene

import engine.core.render.model.Model

data class TileSelectionData(
    val width: Int,
    val height: Int,
    val extraModel: Model?
)
