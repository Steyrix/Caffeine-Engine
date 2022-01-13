package engine.core.shader

import org.lwjgl.opengl.GL20.glCreateProgram
import org.lwjgl.opengl.GL20.glGetUniformLocation
import java.lang.IllegalStateException

class Shader {
    private val id = glCreateProgram()
    private var vertexShaderId = 0
    private var fragmentShaderId = 0

    private val uniforms = hashMapOf<String, Int>()

    init {
        if (id == 0) {
            throw IllegalStateException("Could not create shader program")
        }
    }

    fun createUniform(uniformName: String) {
        val uniformLocation = glGetUniformLocation(id, uniformName)
    }
}