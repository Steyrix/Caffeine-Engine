package engine.core.render.primitive

import engine.core.render.render2D.OpenGlObject2D
import engine.feature.util.Buffer

class Rectangle(r: Float, g: Float, b: Float) : OpenGlObject2D(
        bufferParamsCount = 2,
        dataArrays = listOf(
                Buffer.RECTANGLE_INDICES, Buffer.getColorBuffer(r, g, b)
        ),
        verticesCount = 6
)