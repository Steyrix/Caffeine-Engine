package engine.feature.collision.boundingbox

import engine.core.render.render2D.Drawable2D
import engine.core.render.render2D.Vertexed2D
import engine.core.shader.Shader
import engine.feature.matrix.MatrixComputer
import engine.feature.util.Buffer
import org.lwjgl.opengl.GL33C.*

// TODO: Implement cloneable interface
// TODO: Implement collider pattern
open class BoundingBox(
        override var x: Float,
        override var y: Float,
        override var xSize: Float,
        override var ySize: Float,
) : IntersectableBox, Drawable2D,
        Vertexed2D(
                bufferParamsCount = 1,
                dataArrays = listOf(Buffer.RECTANGLE_VERTICES),
                verticesCount = 8) {

    override var shader: Shader? = null
    override val innerDrawableComponents: MutableList<Drawable2D> = mutableListOf()
    override var rotationAngle: Float = 0f

    override fun draw() {
        shader?.let {
            it.bind()

            val model = MatrixComputer.getResultMatrix(x, y, xSize, ySize, rotationAngle)
            it.setUniform(Shader.VAR_KEY_MODEL, model)

            glBindVertexArray(vertexArrayHandle)
            glDrawArrays(GL_LINES, 0, verticesCount)
        }
    }
}