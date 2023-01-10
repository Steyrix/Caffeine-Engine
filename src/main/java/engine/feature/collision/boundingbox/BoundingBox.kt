package engine.feature.collision.boundingbox

import engine.core.entity.Entity
import engine.core.render.render2D.Drawable2D
import engine.core.render.render2D.Vertexed2D
import engine.core.shader.Shader
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import engine.core.update.SetOfParameters
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
        var rotationAngle: Float = 0f
) : IntersectableBox, Entity,
        Vertexed2D(
                bufferParamsCount = 1,
                dataArrays = listOf(Buffer.RECTANGLE_VERTICES),
                verticesCount = 8) {

    override var shader: Shader? = null
    override val innerDrawableComponents: MutableList<Drawable2D> = mutableListOf()

    override fun draw() {
        shader?.let {
            it.bind()

            val model = MatrixComputer.getResultMatrix(x, y, xSize, ySize, rotationAngle)
            it.setUniform(Shader.VAR_KEY_MODEL, model)

            glBindVertexArray(vertexArrayHandle)
            glDrawArrays(GL_LINES, 0, verticesCount)
        }
    }

    override fun updateParameters(parameters: SetOfParameters) {
        if (parameters is SetOfStatic2DParameters) {
            parameters.let {
                x = it.x
                y = it.y
                xSize = it.xSize
                ySize = it.ySize
                rotationAngle = it.rotationAngle
            }
        }

        if (parameters is SetOf2DParametersWithVelocity) {
            parameters.let {
                x = it.x
                y = it.y
                xSize = it.xSize
                ySize = it.ySize
                rotationAngle = it.rotationAngle
            }
        }
    }
}