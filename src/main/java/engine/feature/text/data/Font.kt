package engine.feature.text.data

import engine.core.render.render2D.OpenGlObject2D

data class Font(
        val name: String,
        val symbolAtlasPath: String,
        val atlasGraphicObject: OpenGlObject2D
)
