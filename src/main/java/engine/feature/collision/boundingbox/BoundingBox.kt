package engine.feature.collision.boundingbox

import engine.core.entity.Entity
import engine.core.render.Drawable2D
import engine.core.render.Vertexed2D
import engine.core.shader.Shader
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParametersWithOffset
import engine.feature.matrix.MatrixComputer
import engine.feature.util.DefaultBufferData
import org.lwjgl.opengl.GL33C.*

// TODO: Implement cloneable interface
open class BoundingBox(
        override var x: Float = 0f,
        override var y: Float = 0f,
        override var xSize: Float = 0f,
        override var ySize: Float = 0f,
        override var xOffset: Float  = 0f,
        override var yOffset: Float = 0f,
        private var rotationAngle: Float = 0f,
        private val isSizeBoundToHolder: Boolean = true
) : IntersectableBox, Entity,
        Vertexed2D(
                bufferParamsCount = 1,
                dataArrays = listOf(DefaultBufferData.RECTANGLE_VERTICES),
                verticesCount = 8) {

    constructor(initialParams: SetOfStatic2DParametersWithOffset, isSizeBoundToHolder: Boolean) :
            this(
                    initialParams.x,
                    initialParams.y,
                    initialParams.xSize,
                    initialParams.ySize,
                    initialParams.xOffset,
                    initialParams.yOffset,
                    initialParams.rotationAngle,
                    isSizeBoundToHolder
            )

    override var shader: Shader? = null
    override val innerDrawableComponents: MutableList<Drawable2D> = mutableListOf()

    override fun draw() {
        shader?.let {
            it.bind()

            val model = MatrixComputer.getResultMatrix(x, y, xSize, ySize, rotationAngle)
            it.setUniform(Shader.VAR_KEY_MODEL, model)

            vertexArray.bind()
            glDrawArrays(GL_LINES, 0, verticesCount)
        }
    }

    override fun updateParameters(parameters: SetOfParameters) {
        if (parameters is SetOfStatic2DParameters) {
            parameters.let {
                x = it.x + xOffset
                y = it.y + yOffset
                if (isSizeBoundToHolder) {
                    xSize = it.xSize
                    ySize = it.ySize
                }
                rotationAngle = it.rotationAngle
            }
        }

        if (parameters is SetOf2DParametersWithVelocity) {
            parameters.let {
                x = it.x + xOffset
                y = it.y + yOffset
                if (isSizeBoundToHolder) {
                    xSize = it.xSize
                    ySize = it.ySize
                }
                rotationAngle = it.rotationAngle
            }
        }
    }

    fun getParameters(): SetOfStatic2DParameters {
        return SetOfStatic2DParameters(
                x, y, xSize, ySize, rotationAngle
        )
    }

    fun forceUpdateSize(width: Float, height: Float) {
        xSize = width
        ySize = height
    }
}