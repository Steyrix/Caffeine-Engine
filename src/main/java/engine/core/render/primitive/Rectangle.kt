package engine.core.render.primitive

import engine.core.render.OpenGlObject2D
import engine.feature.util.DefaultBufferData

class Rectangle(r: Float, g: Float, b: Float) : OpenGlObject2D(
        bufferParamsCount = 2,
        dataArrays = listOf(
                DefaultBufferData.RECTANGLE_INDICES, DefaultBufferData.getColorBuffer(r, g, b)
        ),
        verticesCount = 6
)