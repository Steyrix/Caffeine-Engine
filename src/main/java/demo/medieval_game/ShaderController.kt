package demo.medieval_game

import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import org.joml.Matrix4f

object ShaderController {
    fun createBoundingBoxShader(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/boundingBoxShaders/boundingBoxVertexShader.glsl")!!.path
        val fragmentPath =
            this.javaClass.getResource("/shaders/boundingBoxShaders/boundingBoxFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
            vertexShaderFilePath = vertexPath,
            fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }

    fun createAnimationShader(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/animationShaders/animVertexShader.glsl")!!.path
        val fragmentPath = this.javaClass.getResource("/shaders/animationShaders/animFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
            vertexShaderFilePath = vertexPath,
            fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }

    fun createAnimationShaderWithTexArray(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/animationShaders/animTexArrayVertexShader.glsl")!!.path
        val fragmentPath =
            this.javaClass.getResource("/shaders/animationShaders/animTexArrayFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
            vertexShaderFilePath = vertexPath,
            fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }

    fun createTexturedShader(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/texturedShaders/texturedVertexShader.glsl")!!.path
        val fragmentPath = this.javaClass.getResource("/shaders/texturedShaders/texturedFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
            vertexShaderFilePath = vertexPath,
            fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }

    fun createHpBarShader(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/texturedShaders/hpBarVertexShader.glsl")!!.path
        val fragmentPath = this.javaClass.getResource("/shaders/texturedShaders/hpBarFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
            vertexShaderFilePath = vertexPath,
            fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }

    fun createPrimitiveShader(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/primitiveShaders/primitiveVertexShader.glsl")!!.path
        val fragmentPath = this.javaClass.getResource("/shaders/primitiveShaders/primitiveFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
            vertexShaderFilePath = vertexPath,
            fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }

    fun createTextShader(projection: Matrix4f): Shader {
        val vertexPath = this.javaClass.getResource("/shaders/textShaders/textRenderVertexShader.glsl")!!.path
        val fragmentPath = this.javaClass.getResource("/shaders/textShaders/textRenderFragmentShader.glsl")!!.path

        return ShaderLoader.loadFromFile(
            vertexShaderFilePath = vertexPath,
            fragmentShaderFilePath = fragmentPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, projection)
        }
    }
}