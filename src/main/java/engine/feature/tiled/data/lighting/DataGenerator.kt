package engine.feature.tiled.data.lighting

import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileMap
import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.abs
import kotlin.math.min

internal object DataGenerator {

    private const val AMBIENT_VALUE = 0.4f
    internal const val DEFAULT_RADIUS = 0.1f
    internal const val INTENSITY_CAP = 1.5f

    private fun getGraphicalComponent(
        tileMap: TileMap,
        lightSources: List<SetOfStatic2DParameters>,
        lightSourceTargetRadius: Float = DEFAULT_RADIUS,
        lightIntensityCap: Float = INTENSITY_CAP,
        screenSizeX: Float,
        screenSizeY: Float
    ): Model {
        val lightPerTileList = mutableListOf<Vector3f>()

        val tilesCount = tileMap.tilesCount
        val tilePositions = mutableListOf<Point2D>()

        for (index in 0 until tilesCount) {
            tilePositions.add(
                tileMap.getTilePosition(index)
            )
        }

        val tileVectors = tilePositions.map {
            val x = it.x / screenSizeX * 2 - 1
            val y = -it.y / screenSizeY * 2 + 1
            Vector2f(x, y)
        }

        val lightsVectors = lightSources.map {
            val horizontalDiff = -it.xSize / 2
            val verticalDiff = it.ySize
            val x = (it.x - horizontalDiff) / screenSizeX * 2 - 1
            val y = (-it.y + verticalDiff) / screenSizeY * 2 + 1
            Vector2f(x, y)
        }

        tileVectors.forEach { tile ->
            var diffusionValue = 0f
            var totalIntensity = 0f

            lightsVectors.forEach { lightSource ->
                val distance = lightSource.distance(tile)
                val intensity = 1f / distance

                if (distance < lightSourceTargetRadius) {
                    diffusionValue = 1f - abs(distance / lightSourceTargetRadius)
                }

                if (totalIntensity == 0f) {
                    totalIntensity = intensity
                } else {
                    totalIntensity += intensity
                }
            }
            if (totalIntensity >= lightIntensityCap) {
                totalIntensity = lightIntensityCap
            }
            if (totalIntensity <= AMBIENT_VALUE) {
                totalIntensity = AMBIENT_VALUE
            }

            val out = min(totalIntensity * diffusionValue + AMBIENT_VALUE, 1f)

            lightPerTileList.add(
                Vector3f(out, out, out)
            )
        }

        val colorBuffer = convertToBuffer(lightPerTileList)
        val verticesBuffer = tileMap.getVertices()

        return Model(
            dataArrays = listOf(verticesBuffer.toFloatArray(), colorBuffer),
            verticesCount = verticesBuffer.size / 2
        )
    }

    private fun convertToBuffer(list: List<Vector3f>): FloatArray {
        val out = mutableListOf<Float>()
        list.forEach {
            out.addAll(
                listOf(
                    it.x, it.y, it.z,
                    it.x, it.y, it.z,
                    it.x, it.y, it.z,
                    it.x, it.y, it.z,
                    it.x, it.y, it.z,
                    it.x, it.y, it.z
                )
            )
        }
        return out.toFloatArray()
    }

    fun generateInstance(
        tileMap: TileMap,
        lightSources: List<SetOfStatic2DParameters>,
        lightSourceTargetRadius: Float = DEFAULT_RADIUS,
        lightIntensityCap: Float = INTENSITY_CAP,
        screenSizeX: Float,
        screenSizeY: Float
    ): Texture2D {

        val model = getGraphicalComponent(
            tileMap,
            lightSources,
            lightSourceTargetRadius,
            lightIntensityCap,
            screenSizeX,
            screenSizeY
        )

        return Texture2D.createInstance(screenSizeX, screenSizeY, model)
    }
}