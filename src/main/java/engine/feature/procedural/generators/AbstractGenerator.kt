package engine.feature.procedural.generators

import engine.core.geometry.Point2D

abstract class AbstractGenerator {

    abstract var noiseFunc: (Long, Double, Double) -> Float

    protected fun getNoiseForCoordinateWithFrequency(
        seed: Long,
        pos: Point2D,
        frequency: Double = 1.0,
    ): Float {
        return noiseFunc(
            seed,
            pos.x.toDouble() * frequency,
            pos.y.toDouble() * frequency
        )
    }

    protected fun getNoiseForCoordinateWithWaveLength(
        seed: Long,
        pos: Point2D,
        wavelength: Double = 1.0,
    ): Float {
        return noiseFunc(
            seed,
            pos.x.toDouble() * wavelength,
            pos.y.toDouble() * wavelength
        )
    }
}