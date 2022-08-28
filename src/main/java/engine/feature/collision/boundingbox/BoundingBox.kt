package engine.feature.collision.boundingbox

import engine.core.render.Drawable
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
) : IntersectableBox, Drawable,
        Vertexed2D(
                bufferParamsCount = 1,
                dataArrays = listOf(Buffer.RECTANGLE_VERTICES),
                verticesCount = 8) {
    override fun draw2D(
            x: Float,
            y: Float,
            xSize: Float,
            ySize: Float,
            rotationAngle: Float,
            shader: Shader
    ) {
        TODO("Not yet implemented")
    }
}