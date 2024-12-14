package engine.feature.text.data

import engine.core.render.model.Model

data class Font(
    val name: String,
    val symbolAtlasPath: String,
    val atlasGraphicObject: Model
)
