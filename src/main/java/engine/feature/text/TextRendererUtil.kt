package engine.feature.text

import engine.core.texture.Texture2D
import engine.core.geometry.Point2D
import java.awt.Dimension

internal object TextRendererUtil {

    fun generateMap(
        charSize: Dimension,
        textureAtlas: Texture2D,
        characters: List<Char>
    ): HashMap<Char, Point2D> {
        val out = HashMap<Char, Point2D>()

        var j = 0
        var k = 0

        for (element in characters) {
            if (charSize.getWidth() * j >= textureAtlas.getWidthF()) {
                j = 0
                k++
            }
            out[element] = Point2D(j++.toFloat(), k.toFloat())
        }

        return out
    }
}