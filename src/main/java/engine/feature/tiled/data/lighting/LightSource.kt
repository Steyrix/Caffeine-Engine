package engine.feature.tiled.data.lighting

import engine.core.update.SetOfStatic2DParameters

interface LightSource {

    val parameters: SetOfStatic2DParameters

    val radius: Float

    val intensityCap: Float
}