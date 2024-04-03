package engine.feature.tiled.data.lighting

import engine.core.update.SetOfParameters

interface LightSource {

    val radius: Float

    val intensityCap: Float

    var isLit: Boolean

    fun getParameters(): SetOfParameters
}