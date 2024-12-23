package engine.feature.tiled.scene

import engine.core.render.shader.Shader
import engine.core.render.shader.ShaderLoader
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.TileMapShaders
import engine.feature.tiled.parser.TiledResourceParser
import org.joml.Matrix4f
import java.io.File

internal object TileMapGraphicsProvider {

    fun getShaders(
        mapPresets: ProceduralMapPreset,
        renderProjection: Matrix4f
    ): TileMapShaders {

        val vertexShaderPath = this.javaClass.getResource(mapPresets.vertexShaderPath)?.path
            ?: throw IllegalStateException()

        val fragmentShaderPath = this.javaClass.getResource(mapPresets.fragmentShaderPath)?.path
            ?: throw IllegalStateException()

        val vertexObjectShaderPath = this.javaClass.getResource(mapPresets.objectVertexShaderPath)?.path
            ?: throw IllegalStateException()

        val fragmentObjectShaderPath = this.javaClass.getResource(mapPresets.objectFragmentShaderPath)?.path
            ?: throw IllegalStateException()

        val shaders = compileBaseShaders(
            vertexShaderPath,
            fragmentShaderPath,
            vertexObjectShaderPath,
            fragmentObjectShaderPath,
            renderProjection,
            mapPresets.shaderUniforms
        )

        // TODO: cover with debug flag
        // TODO: remove hardcode
        val debugVertexShaderPath =
            this.javaClass.getResource("/shaders/debugShaders/mapDebugVertexShader.glsl")?.path
                ?: throw IllegalStateException()

        val debugFragmentShaderPath =
            this.javaClass.getResource("/shaders/debugShaders/mapDebugFragmentShader.glsl")?.path
                ?: throw IllegalStateException()

        val debugShader = ShaderLoader.loadFromFile(
            vertexShaderFilePath = debugVertexShaderPath,
            fragmentShaderFilePath = debugFragmentShaderPath
        ).also { shader ->
            shader.bind()
            shader.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection)
        }

        shaders.debugShader = debugShader

        return shaders
    }

    fun getShaders(
        mapPresets: TileMapPreset,
        renderProjection: Matrix4f
    ): TileMapShaders {
        val vertexShaderPath = this.javaClass.getResource(mapPresets.vertexShaderPath)?.path
            ?: throw IllegalStateException()

        val fragmentShaderPath = this.javaClass.getResource(mapPresets.fragmentShaderPath)?.path
            ?: throw IllegalStateException()

        val vertexObjectShaderPath = this.javaClass.getResource(mapPresets.objectVertexShaderPath)?.path
            ?: throw IllegalStateException()

        val fragmentObjectShaderPath = this.javaClass.getResource(mapPresets.objectFragmentShaderPath)?.path
            ?: throw IllegalStateException()

        val shaders = compileBaseShaders(
            vertexShaderPath,
            fragmentShaderPath,
            vertexObjectShaderPath,
            fragmentObjectShaderPath,
            renderProjection,
            mapPresets.shaderUniforms
        )

        // TODO: cover with debug flag
        // TODO: remove hardcode
        val debugVertexShaderPath =
            this.javaClass.getResource("/shaders/debugShaders/mapDebugVertexShader.glsl")?.path
                ?: throw IllegalStateException()

        val debugFragmentShaderPath =
            this.javaClass.getResource("/shaders/debugShaders/mapDebugFragmentShader.glsl")?.path
                ?: throw IllegalStateException()

        val debugShader = ShaderLoader.loadFromFile(
            vertexShaderFilePath = debugVertexShaderPath,
            fragmentShaderFilePath = debugFragmentShaderPath
        ).also { shader ->
            shader.bind()
            shader.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection)
        }

        shaders.debugShader = debugShader

        return shaders
    }

    fun getGraphicalComponent(
        mapPresets: TileMapPreset,
        renderProjection: Matrix4f
    ): TileMap {
        val sourcePath = this.javaClass.getResource(mapPresets.mapSourcePath)?.path
            ?: throw IllegalStateException()
        val graphicalComponent = TiledResourceParser.createTileMapFromXml(
            File(sourcePath)
        )

        graphicalComponent.shaders = getShaders(mapPresets, renderProjection)

        return graphicalComponent
    }

    private fun compileBaseShaders(
        vertexShaderPath: String,
        fragmentShaderPath: String,
        vertexObjectShaderPath: String,
        fragmentObjectShaderPath: String,
        renderProjection: Matrix4f,
        shaderUniforms: Map<String, Any>
    ): TileMapShaders  {
        val mainShader = ShaderLoader.loadFromFile(
            vertexShaderFilePath = vertexShaderPath,
            fragmentShaderFilePath = fragmentShaderPath
        ).also { shader ->
            shader.bind()
            shader.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection)

            shaderUniforms.forEach {
                shader.setUniform(it.key, it.value)
            }
        }

        val objectShaderCreator = {
            ShaderLoader.loadFromFile(
                vertexShaderFilePath = vertexObjectShaderPath,
                fragmentShaderFilePath = fragmentObjectShaderPath
            ).also { shader ->
                shader.bind()
                shader.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection)
            }
        }

        return TileMapShaders(mainShader, objectShaderCreator)
    }
}