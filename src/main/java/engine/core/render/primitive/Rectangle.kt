package engine.core.render.primitive

import engine.core.render.model.Model
import engine.core.render.util.DefaultBufferData

class Rectangle(r: Float, g: Float, b: Float) : Model(
    listOf(
        DefaultBufferData.RECTANGLE_INDICES,
        DefaultBufferData.getColorBuffer(r, g, b)
    ),
    verticesCount = 6
)
