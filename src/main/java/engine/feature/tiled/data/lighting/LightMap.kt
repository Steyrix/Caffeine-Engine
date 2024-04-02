package engine.feature.tiled.data.lighting

import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.render.util.DefaultBufferData
import engine.core.shader.Shader
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileMap
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.*

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

        graphicalComponent = Model(
            texture = texture,
            uv = DefaultBufferData.getRectangleSectorVerticesReversed(1f, 1f)
        ).apply {
            preDrawFunc = {
                glBlendFunc(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA)
            }
            postDrawFunc = {
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
            }
        }

        addComponent(graphicalComponent, parameters)
    }

    fun setShader(
        shader: Shader,
    ) {
        graphicalComponent.shader = shader
    }
}