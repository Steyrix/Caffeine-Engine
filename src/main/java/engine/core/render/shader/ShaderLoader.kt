package engine.core.render.shader

import java.nio.charset.Charset
import java.nio.file.Files
import kotlin.io.path.Path

object ShaderLoader {

    fun loadFromFile(
        vertexShaderFilePath: String,
        fragmentShaderFilePath: String
    ): Shader {
        return Shader().apply {
            val encodedVertexShader = Files.readAllBytes(Path(vertexShaderFilePath))
            val encodedFragmentShader = Files.readAllBytes(Path(fragmentShaderFilePath))
            val charset = Charset.defaultCharset()
            createVertexShader(String(encodedVertexShader, charset))
            createFragmentShader(String(encodedFragmentShader, charset))
            link()
        }
    }
}