package engine.feature.collision.bounding_box

import engine.core.entity.CompositeEntity
import engine.core.geometry.Point2D
import engine.core.render.interfaces.Drawable
import engine.core.render.Model
import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters
import engine.core.update.SetOfStatic2DParametersWithOffset
import engine.core.render.util.DefaultBufferData
import engine.core.update.SetOfParameters
import org.lwjgl.opengl.GL33C.*

// TODO: Implement cloneable interface
class BoundingBox(
    override var x: Float = 0f,
    override var y: Float = 0f,
    override var xSize: Float = 0f,
    override var ySize: Float = 0f,
    override var xOffset: Float = 0f,
    override var yOffset: Float = 0f,
    private var rotationAngle: Float = 0f,
    private val isSizeBoundToHolder: Boolean = true
) : CompositeEntity(), IntersectableBox, Drawable<SetOfParameters> {

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

    constructor(
        x: Float,
        y: Float,
        offsets: OffsetData,
        rotationAngle: Float = 0f,
        isSizeBoundToHolder: Boolean = true
    ) :
            this(
                x = x,
                y = y,
                offsets.xSize,
                offsets.ySize,
                offsets.xOffset,
                offsets.yOffset,
                rotationAngle,
                isSizeBoundToHolder
            )

    private val model = Model(
        dataArrays = listOf(DefaultBufferData.RECTANGLE_VERTICES),
        verticesCount = 8
    ).apply {
        drawMode = GL_LINES
    }

    override var shader: Shader? = null
        set(value) {
            model.shader = value
            field = value
        }

    override var zLevel: Float = 1f

    private var params = SetOfStatic2DParameters(
        x,
        y,
        xSize,
        ySize,
        rotationAngle
    )

    var isDebugMeshEnabled: Boolean = false
    set(value) {
        if (value) {
            addComponent(model, params)
        } else {
            removeComponent(model)
        }
        field = value
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

        params.x = x
        params.y = y
        params.xSize = xSize
        params.ySize = ySize
        params.rotationAngle = rotationAngle
    }

    fun getParameters(): SetOfStatic2DParameters {
        return params
    }

    fun forceUpdateSize(width: Float, height: Float) {
        xSize = width
        ySize = height
    }

    fun getActualPosition(): Point2D {
        return Point2D(
            x = params.x + xOffset,
            y = params.y + yOffset
        )
    }

    fun getActualParameters(): SetOfStatic2DParameters {
        return params.copy(
            x = x + xOffset,
            y = y + yOffset
        )
    }

    fun applyOffsets(offsetData: OffsetData) {
        xOffset = offsetData.xOffset
        yOffset = offsetData.yOffset
        xSize = offsetData.xSize
        ySize = offsetData.ySize
    }
}