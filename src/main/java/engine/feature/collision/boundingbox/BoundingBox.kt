package engine.feature.collision.boundingbox

import engine.feature.util.Buffer
import org.lwjgl.opengl.GL33C.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

// TODO: Implement cloneable interface
// TODO: Implement collider pattern
open class BoundingBox(
        posX: Float,
        posY: Float,
        width: Float,
        height: Float,
) : IntersectableBox(posX, posY, width, height) {

    val vertexBuffer = IntBuffer.allocate(1)
    val vertexArray = IntBuffer.allocate(1)

    fun initVertexBuffer() {
        glGenBuffers(IntBuffer.allocate(1))

        val bbVerticesArray = Buffer.RECTANGLE_VERTICES
        val bbVerticesBuffer = FloatBuffer.wrap(bbVerticesArray)

        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer.get(0))
        glBufferData(GL_ARRAY_BUFFER, bbVerticesBuffer, GL_STATIC_DRAW)
    }

    fun initVertexArray() {
        glGenVertexArrays(vertexArray)
        glBindVertexArray(vertexArray.get(0))

        glEnableVertexAttribArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer.get(0))
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
    }
}