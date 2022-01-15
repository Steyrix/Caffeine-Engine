package engine.core.shader

import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*
import org.lwjgl.system.MemoryStack
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

// TODO: move exception messages to constants
// TODO: move magic numbers to constants
class Shader {
    private val programId = glCreateProgram()
    private var vertexShaderId = 0
    private var fragmentShaderId = 0

    private val uniforms = hashMapOf<String, Int>()

    init {
        if (programId == 0) {
            throw IllegalStateException("Could not create shader program")
        }
    }

    fun createUniform(uniformName: String) {
        val uniformLocation = glGetUniformLocation(programId, uniformName)
        if (uniformLocation < 0) {
            throw IllegalArgumentException("Could not find uniform with name $uniformName")
        }

        uniforms[uniformName] = uniformLocation
    }

    fun setUniform(uniformName: String, value: Matrix4f) {
        MemoryStack.stackPush().use {
            checkForUniformNameExists(uniformName)

            glUniformMatrix4fv(
                uniforms[uniformName]!!,
                false,
                value.get(it.mallocFloat(16))
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

    fun createVertexShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER)
    }

    fun createFragmentShader(shaderCode: String) {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER)
    }

    protected fun createShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType)
        if (shaderId == 0) {
            throw IllegalStateException("Could not create shader with type $shaderType")
        }

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw IllegalStateException("Could not compile shader: ${glGetShaderInfoLog(shaderId, 1024)}")
        }

        glAttachShader(programId, shaderId)

        return shaderId
    }

    fun link() {
        glLinkProgram(programId)
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw IllegalStateException("Could not link shader: ${glGetShaderInfoLog(programId, 1024)}")
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId)
        }

        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId)
        }

        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            throw IllegalStateException("Could not validate shader: ${glGetShaderInfoLog(programId, 1024)}")
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
        require(uniforms.containsKey(uniformName)) {
            throw IllegalArgumentException("Could not find uniform with name $uniformName")
        }
    }
}