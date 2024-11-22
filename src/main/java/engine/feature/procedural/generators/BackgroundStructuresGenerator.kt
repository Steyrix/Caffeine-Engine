package engine.feature.procedural.generators

import engine.core.geometry.Point2D
import engine.feature.procedural.autotiling.Autotiler

class BackgroundStructuresGenerator(
    private val genericNoiseCondition: (noiseValue: Float) -> Boolean,
    private val bitTValueToId: Map<Int,Int>,
    private val layerWidthInTiles: Int,
    override var noiseFunc: (Long, Double, Double) -> Float
) : AbstractGenerator() {

    fun generate(
        seed: Long,
        worldData: List<Point2D>,
    ): List<Int> {
        val noiseResult: PointToNoiseValueList = mutableListOf()

        worldData.forEach {
            val noiseValue = getNoiseForCoordinate(
                seed, it
            )

            if (genericNoiseCondition(noiseValue)) {
                noiseResult.add(it to noiseValue)
            } else {
                noiseResult.add(it to 0f)
            }
        }

        return performAutotiling(noiseResult)
    }

    private fun performAutotiling(pointToNoiseValueList: PointToNoiseValueList): List<Int> {
        val binaryData = pointToNoiseValueList.map {
            if (it.second != 0f) 1 else 0
        }

        return Autotiler.assignTiles(
            bitTValueToId,
            binaryData,
            layerWidthInTiles
        )
    }
}