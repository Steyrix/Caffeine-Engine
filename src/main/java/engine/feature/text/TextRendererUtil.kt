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
        val charsCount: Int

        val xStep: Int = (textureAtlas.getWidthF() / charSize.getWidth()).toInt()
        val yStep: Int = (textureAtlas.getHeightF() / charSize.getHeight()).toInt()
        charsCount = xStep * yStep

        var j = 0
        var k = 0

        for (i in 0 until charsCount) {
            if (charSize.getWidth() * j >= textureAtlas.getWidthF()) {
                j = 0
                k++
            }
            out[characters[i]] = Point2D(j++.toFloat(), k.toFloat())
        }

        return out
    }
}