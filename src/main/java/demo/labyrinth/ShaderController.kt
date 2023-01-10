package demo.labyrinth

import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import org.joml.Matrix4f

object ShaderController {
    fun createBoundingBoxShader(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/boundingBoxVertexShader.glsl")!!.path
        val fragmentPath = this.javaClass.getResource("/shaders/boundingBoxFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
                vertexShaderFilePath = vertexPath,
                fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }

    fun createAnimationShader(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/animVertexShader.glsl")!!.path
        val fragmentPath = this.javaClass.getResource("/shaders/animFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
                vertexShaderFilePath = vertexPath,
                fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }

    fun createAnimationShaderWithTexArray(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/animTexArrayVertexShader.glsl")!!.path
        val fragmentPath = this.javaClass.getResource("/shaders/animTexArrayFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
                vertexShaderFilePath = vertexPath,
                fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }

    fun createTexturedShader(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/texturedVertexShader.glsl")!!.path
        val fragmentPath = this.javaClass.getResource("/shaders/texturedFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
                vertexShaderFilePath = vertexPath,
                fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }

    fun createPrimitiveShader(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/primitiveVertexShader.glsl")!!.path
        val fragmentPath = this.javaClass.getResource("/shaders/primitiveFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
                vertexShaderFilePath = vertexPath,
                fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }
}