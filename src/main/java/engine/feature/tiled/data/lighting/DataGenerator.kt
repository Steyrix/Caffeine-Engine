package engine.feature.tiled.data.lighting

import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import engine.core.texture.Texture2D
import engine.feature.tiled.data.TileMap
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.abs
import kotlin.math.min

internal object DataGenerator {

    private const val AMBIENT_VALUE = 0.4f
    private const val INTENSITY_CAP = 1.5f

    private fun getGraphicalComponent(
        tileMap: TileMap,
        lightSources: List<LightSource>,
        screenSizeX: Float,
        screenSizeY: Float,
        translation: Vector2f
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
            Vector2f(x + translation.x, y + translation.y)
        }

        tileVectors.forEach { tile ->
            var diffusionValue = 0f
            var totalIntensity = 0f

            lightSources.forEach { src ->
                val lightSource = getVector(src, screenSizeX, screenSizeY, translation)

                val distance = lightSource.distance(tile)
                val intensity = 1f / distance

                if (distance < src.radius) {
                    diffusionValue += 1f - abs(distance / src.radius)
                }

                if (totalIntensity == 0f) {
                    totalIntensity = intensity
                } else {
                    totalIntensity += intensity
                }
            }

            // TODO: ref
            val intensityCap = lightSources.minOfOrNull { it.intensityCap } ?: INTENSITY_CAP
            if (totalIntensity >= intensityCap) {
                totalIntensity = intensityCap
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

    private fun getVector(
        it: LightSource,
        screenSizeX: Float,
        screenSizeY: Float,
        translation: Vector2f
    ): Vector2f {
        val horizontalDiff = -it.getParameters().xSize / 2
        val verticalDiff = it.getParameters().ySize
        val x = (it.getParameters().x - horizontalDiff) / screenSizeX * 2 - 1
        val y = (-it.getParameters().y + verticalDiff) / screenSizeY * 2 + 1
        return Vector2f(x + translation.x, y + translation.y)
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

    // TODO: pass from outside
    private fun createShader(projection: Matrix4f): Shader {
        return ShaderLoader.loadFromFile(
            this.javaClass.getResource("/shaders/utilityVertexShader.glsl")!!.path,
            this.javaClass.getResource("/shaders/utilityFragmentShader.glsl")!!.path
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }

    fun generateInstance(
        precision: Float,
        projection: Matrix4f,
        tileMap: TileMap,
        lightSources: List<LightSource>,
        screenSizeX: Float,
        screenSizeY: Float,
        translation: Vector2f
    ): Texture2D {

        val shader = createShader(projection)

        val model = getGraphicalComponent(
            tileMap,
            lightSources,
            screenSizeX,
            screenSizeY,
            translation
        ).apply {
            this.shader = shader
            x = 0f
            y = 0f
            xSize = precision
            ySize = precision
            rotationAngle = 0f
            isPartOfWorldTranslation = false
        }

        return Texture2D.createInstance(precision, screenSizeX, screenSizeY, model)
    }
}