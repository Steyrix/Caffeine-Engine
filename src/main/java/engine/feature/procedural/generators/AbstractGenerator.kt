package engine.feature.procedural.generators

import engine.core.geometry.Point2D

abstract class AbstractGenerator {

    abstract var noiseFunc: (Long, Double, Double) -> Float

    protected fun getNoiseForCoordinate(
        seed: Long,
        pos: Point2D
    ): Float {
        return noiseFunc(
            seed,
            pos.x.toDouble(),
            pos.y.toDouble()
        )
    }
}