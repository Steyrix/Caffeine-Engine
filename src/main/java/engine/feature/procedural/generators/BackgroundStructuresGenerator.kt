package engine.feature.procedural.generators

import engine.core.geometry.Point2D

class BackgroundStructuresGenerator(
    private val genericNoiseCondition: (noiseValue: Float) -> Boolean
) : AbstractGenerator() {

    override var noiseFunc: (Long, Double, Double) -> Float = { _, _, _ ->
        0f
    }

    fun generate(
        seed: Long,
        worldData: List<Point2D>,
    ): ProceduralData {
        val result: ProceduralData = mutableListOf()

        worldData.forEach {
            val noiseValue = getNoiseForCoordinate(
                seed, it
            )

            if (genericNoiseCondition(noiseValue)) {
                result.add(it to noiseValue)
            } else {
                result.add(it to 0f)
            }
        }

        return result
    }
}