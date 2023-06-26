package engine.feature.collision.boundingbox

import engine.core.entity.Entity
import engine.core.render.Drawable
import engine.core.render.Mesh
import engine.core.render.Model
import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters
import engine.core.update.SetOfStatic2DParametersWithOffset
import engine.core.render.util.DefaultBufferData
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfParameters
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
) : IntersectableBox, Entity, Drawable<SetOfParameters> {

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

    private val model = Model(
            mesh = Mesh(
                    dataArrays = listOf(DefaultBufferData.RECTANGLE_VERTICES),
                    verticesCount = 8
            )
    ).apply {
        drawMode = GL_LINES
    }

    override var shader: Shader? = null
        set(value) {
            model.shader = value
            field = value
        }

    override fun draw() {
        model.draw()
    }

    override fun updateParameters(parameters: SetOfParameters) {
        parameters.let {
            x = it.x + xOffset
            y = it.y + yOffset
            if (isSizeBoundToHolder) {
                xSize = it.xSize
                ySize = it.ySize
            }
            rotationAngle = it.rotationAngle
        }

        model.updateParameters(
                SetOfStatic2DParameters(
                        x, y, xSize, ySize, rotationAngle
                )
        )
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