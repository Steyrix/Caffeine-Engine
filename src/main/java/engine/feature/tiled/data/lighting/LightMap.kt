package engine.feature.tiled.data.lighting

import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.shader.Shader
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileMap
import org.joml.Matrix4f

class LightMap(
    matrix4f: Matrix4f,
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
            matrix4f,
            tileMap,
            lightSources,
            lightSourceTargetRadius,
            lightIntensityCap,
            screenSizeX,
            screenSizeY
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