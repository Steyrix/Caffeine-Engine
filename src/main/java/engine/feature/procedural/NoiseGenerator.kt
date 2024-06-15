package engine.feature.procedural

import engine.core.geometry.Point2D

object NoiseGenerator {

    fun getNoiseForCoordinate(pos: Point2D): Float {
        return OpenSimplex2S.noise2_ImproveX(
            System.currentTimeMillis(), pos.x.toDouble(), pos.y.toDouble()
        )
    }

}