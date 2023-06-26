package engine.core.render.primitive

import engine.core.render.Mesh
import engine.core.render.Model
import engine.core.render.util.DefaultBufferData

class Rectangle(r: Float, g: Float, b: Float) : Model(
        mesh = Mesh(
                dataArrays = listOf(
                        DefaultBufferData.RECTANGLE_INDICES, DefaultBufferData.getColorBuffer(r, g, b)
                ),
                verticesCount = 6
        )
)