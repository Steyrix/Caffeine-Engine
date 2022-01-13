package engine.core.shader

import org.lwjgl.opengl.GL46.*
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

    fun createUniform() {

    }
}