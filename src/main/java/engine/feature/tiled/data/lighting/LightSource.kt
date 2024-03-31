package engine.feature.tiled.data.lighting

import engine.core.update.SetOfParameters

interface LightSource {

    val radius: Float

    val intensityCap: Float

    fun getParameters(): SetOfParameters
}