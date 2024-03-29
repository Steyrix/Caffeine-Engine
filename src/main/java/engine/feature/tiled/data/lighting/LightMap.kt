package engine.feature.tiled.data.lighting

import engine.core.geometry.Point2D
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileMap
import org.joml.Vector2f
import kotlin.math.abs
import kotlin.math.min

object LightMap {

    private const val AMBIENT_VALUE = 0.8f
    private const val DEFAULT_RADIUS = 0.2f
    private const val INTENSITY_CAP = 1.5f

    fun generate(
        tileMap: TileMap,
        lightSources: List<SetOfStatic2DParameters>,
        lightSourceTargetRadius: Float = DEFAULT_RADIUS,
        lightIntensityCap: Float = INTENSITY_CAP
    ) {
        val lightPerTileList = mutableListOf<Float>()

        val tilesCount = tileMap.tilesCount
        val tilePositions = mutableListOf<Point2D>()

        for (index in 0 until tilesCount) {
            tilePositions.add(
                tileMap.getTilePosition(index)
            )
        }

        val tileVectors = tilePositions.map { Vector2f(it.x, it.y) }
        val lightsVectors = lightSources.map { Vector2f(it.x, it.y) }

        var diffusionValue = 0f

        tileVectors.forEach { tile ->

            var totalIntensity = 0f

            lightsVectors.forEach { lightSource ->
                val distance = tile.distance(lightSource)
                var intensity = 1f / distance

                if (distance < lightSourceTargetRadius) {
                    diffusionValue = 1f - abs(distance / lightSourceTargetRadius)
                }

                if (intensity > lightIntensityCap) {
                    intensity = lightIntensityCap
                }

                totalIntensity *= intensity
            }

            if (totalIntensity >= lightIntensityCap) {
                totalIntensity = lightIntensityCap
            }

            val out = min(totalIntensity * diffusionValue + AMBIENT_VALUE, 1.0f)

            lightPerTileList.add(out)
        }
    }
}