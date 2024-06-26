package engine.feature.tiled.scene

import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.parser.TiledResourceParser
import org.joml.Matrix4f
import java.io.File

internal object TileMapGraphicsProvider {

    fun getGraphicalComponent(
        mapPresets: TileMapPreset,
        renderProjection: Matrix4f
    ): TileMap {
        val vertexShaderPath = this.javaClass.getResource(mapPresets.vertexShaderPath)?.path
            ?: throw IllegalStateException()

        val fragmentShaderPath = this.javaClass.getResource(mapPresets.fragmentShaderPath)?.path
            ?: throw IllegalStateException()

        val vertexObjectShaderPath = this.javaClass.getResource(mapPresets.objectVertexShaderPath)?.path
            ?: throw IllegalStateException()

        val fragmentObjectShaderPath = this.javaClass.getResource(mapPresets.objectFragmentShaderPath)?.path
            ?: throw IllegalStateException()

        val sourcePath = this.javaClass.getResource(mapPresets.mapSourcePath)?.path
            ?: throw IllegalStateException()

        val graphicalComponent = TiledResourceParser.createTileMapFromXml(
            File(sourcePath)
        )

        graphicalComponent.shader = ShaderLoader.loadFromFile(
            vertexShaderFilePath = vertexShaderPath,
            fragmentShaderFilePath = fragmentShaderPath
        ).also { shader ->
            shader.bind()
            shader.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection)

            mapPresets.shaderUniforms.forEach {
                shader.setUniform(it.key, it.value)
            }
        }

        graphicalComponent.objectShaderCreator = {
            ShaderLoader.loadFromFile(
                vertexShaderFilePath = vertexObjectShaderPath,
                fragmentShaderFilePath = fragmentObjectShaderPath
            ).also { shader ->
                shader.bind()
                shader.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection)
            }
        }

        // TODO: cover with debug flag
        val debugVertexShaderPath =
            this.javaClass.getResource("/shaders/boundingBoxShaders/boundingBoxVertexShader.glsl")?.path
                ?: throw IllegalStateException()

        val debugFragmentShaderPath =
            this.javaClass.getResource("/shaders/boundingBoxShaders/boundingBoxFragmentShader.glsl")?.path
                ?: throw IllegalStateException()

        graphicalComponent.debugShader = ShaderLoader.loadFromFile(
            vertexShaderFilePath = debugVertexShaderPath,
            fragmentShaderFilePath = debugFragmentShaderPath
        ).also { shader ->
            shader.bind()
            shader.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection)
        }

        return graphicalComponent
    }
}