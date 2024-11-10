package engine.feature.animation

import engine.core.texture.Texture2D

data class AtlasData(
    val frameWidth: Float,
    val frameHeight: Float,
    val animations: List<Animation>,
    val texturePath: String? = null,
    val texture: Texture2D? = null
)