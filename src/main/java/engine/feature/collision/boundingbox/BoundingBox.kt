package engine.feature.collision.boundingbox

import engine.core.render.render2D.Vertexed2D
import engine.feature.util.Buffer

// TODO: Implement cloneable interface
// TODO: Implement collider pattern
open class BoundingBox(
        override var posX: Float,
        override var posY: Float,
        override var width: Float,
        override var height: Float,
) : IntersectableBox,
        Vertexed2D(
                bufferParamsCount = 1,
                dataArrays = listOf(Buffer.RECTANGLE_VERTICES),
                verticesCount = 8)