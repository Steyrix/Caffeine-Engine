package engine.feature.procedural.generators

import engine.core.geometry.Point2D

object BackgroundStructuresGenerator : AbstractGenerator() {

    override var noiseFunc: (Long, Double, Double) -> Float = { _, _, _ ->
        0f
    }

    fun generate(
        seed: Long,
        worldData: List<Point2D>,
    ): ProceduralData {

        worldData.forEach {
            val noiseValue = getNoiseForCoordinate(
                seed, it
            )
        }

        return mutableListOf()
    }
}