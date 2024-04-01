package engine.feature.tiled.data.lighting

import engine.core.entity.CompositeEntity
import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.core.shader.Shader
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileMap
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import kotlin.math.abs
import kotlin.math.min

class LightMap(
    val parameters: SetOfParameters,
    tileMap: TileMap,
    lightSources: List<SetOfStatic2DParameters>,
    lightSourceTargetRadius: Float = DataGenerator.DEFAULT_RADIUS,
    lightIntensityCap: Float = DataGenerator.INTENSITY_CAP,
    screenSizeX: Float,
    screenSizeY: Float
) : CompositeEntity() {


    private var graphicalComponent: Model

    init {
        val texture = DataGenerator.generateInstance(
            tileMap, lightSources, lightSourceTargetRadius, lightIntensityCap, screenSizeX, screenSizeY
        )

        graphicalComponent = Model(texture)

        addComponent(graphicalComponent, parameters)
    }

    fun setShader(
        shader: Shader,
        initialUniformOperation: (Shader) -> Unit,
    ) {
        graphicalComponent.shader = shader
        initialUniformOperation(shader)
    }
}