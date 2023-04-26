package engine.core.shader

import org.joml.Matrix4f
import org.joml.Vector2f
import org.lwjgl.opengl.GL33C.*
import org.lwjgl.system.MemoryStack

class Shader {

    companion object {
        private const val INFO_LOG_MAX_LENGHT = 1024
        private const val MATRIX4F_VALUE_SIZE = 16

        const val VAR_KEY_MODEL = "model"
        const val VAR_KEY_PROJECTION = "projection"
        const val VAR_KEY_TEXTURE_SAMPLE = "textureSample"
        const val VAR_KEY_TEXTURE_ARRAY = "textureArray"
        const val VAR_KEY_TEXTURE_ARRAY_LAYER = "textureArrayLayer"
        const val VAR_KEY_X_OFFSET = "xOffset"
        const val VAR_KEY_Y_OFFSET = "yOffset"
        const val VAR_KEY_FRAME_X = "frameNumberX"
        const val VAR_KEY_FRAME_Y = "frameNumberY"
    }

    private val programId = glCreateProgram()
    private var vertexShaderId = 0
    private var fragmentShaderId = 0

    private val uniforms = hashMapOf<String, Int>()

    init {
        if (programId == 0) {
            throw ShaderNotCreatedException()
        }
    }

    private fun createUniform(uniformName: String) {
        val uniformLocation = glGetUniformLocation(programId, uniformName)
        if (uniformLocation < 0) {
            throw UniformNotFoundException(uniformName)
        }
        uniforms[uniformName] = uniformLocation
    }

    fun setUniform(uniformName: String, value: Matrix4f) {
        MemoryStack.stackPush().use {
            checkForUniformNameExists(uniformName)

            glUniformMatrix4fv(
                uniforms[uniformName]!!,
                false,
                value.get(it.mallocFloat(MATRIX4F_VALUE_SIZE))
            )
        }
    }

    fun setUniform(uniformName: String, value: Vector2f) {
        MemoryStack.stackPush().use {
            checkForUniformNameExists(uniformName)

            glUniform2f(
                    uniforms[uniformName]!!,
                    value.x,
                    value.y
            )
        }
    }

    fun setUniform(uniformName: String, value: Int) {
        MemoryStack.stackPush().use {
            checkForUniformNameExists(uniformName)

            glUniform1i(uniforms[uniformName]!!, value)
        }
    }

    fun setUniform(uniformName: String, value: Float) {
        MemoryStack.stackPush().use {
            checkForUniformNameExists(uniformName)

            glUniform1f(uniforms[uniformName]!!, value)
        }
    }

    fun setUniform(uniformName: String, value: Any) {
        MemoryStack.stackPush().use {
            checkForUniformNameExists(uniformName)

            when (value) {
                is Float -> glUniform1f(uniforms[uniformName]!!, value)
                is Vector2f -> glUniform2f(
                        uniforms[uniformName]!!,
                        value.x,
                        value.y
                )
                is Int -> glUniform1i(uniforms[uniformName]!!, value)
                is Matrix4f -> glUniformMatrix4fv(
                        uniforms[uniformName]!!,
                        false,
                        value.get(it.mallocFloat(MATRIX4F_VALUE_SIZE))
                )
            }
        }
    }

    fun createVertexShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER)
    }

    fun createFragmentShader(shaderCode: String) {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER)
    }

    private fun createShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType)
        if (shaderId == 0) {
            throw IllegalShaderTypeException(shaderType)
        }

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw ShaderCompilationFailedException(glGetShaderInfoLog(shaderId, INFO_LOG_MAX_LENGHT))
        }

        glAttachShader(programId, shaderId)

        return shaderId
    }

    fun link() {
        glLinkProgram(programId)
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw ShaderLinkFailedException(glGetShaderInfoLog(programId, INFO_LOG_MAX_LENGHT))
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId)
        }

        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId)
        }
    }

    fun validate() {
        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            throw ShaderValidationFailedException(glGetShaderInfoLog(programId, INFO_LOG_MAX_LENGHT))
        }
    }

    fun bind() {
        glUseProgram(programId)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun dispose() {
        unbind()
        if (programId != 0) {
            glDeleteProgram(programId)
        }
    }

    private fun checkForUniformNameExists(uniformName: String) {
        if (!uniforms.containsKey(uniformName)) {
            createUniform(uniformName)
        }
        // else throw IllegalArgumentException("Went wrong")
    }
}