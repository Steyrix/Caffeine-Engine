package engine.feature.collision.boundingbox

import engine.core.render.Drawable2D
import engine.core.render.render2D.Vertexed2D
import engine.core.shader.Shader
import engine.feature.util.Buffer

// TODO: Implement cloneable interface
// TODO: Implement collider pattern
open class BoundingBox(
        override var posX: Float,
        override var posY: Float,
        override var width: Float,
        override var height: Float,
) : IntersectableBox, Drawable2D,
        Vertexed2D(
                bufferParamsCount = 1,
                dataArrays = listOf(Buffer.RECTANGLE_VERTICES),
                verticesCount = 8) {

    override var shader: Shader? = null
    override var x: Float = 0f
    override var y: Float = 0f
    override var xSize: Float = 0f
    override var ySize: Float = 0f
    override var rotationAngle: Float = 0f
    override fun draw2D() {
        TODO("Not yet implemented")
    }
}