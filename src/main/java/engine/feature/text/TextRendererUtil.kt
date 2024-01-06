package engine.feature.text

import engine.core.texture.Texture2D
import engine.core.geometry.Point2D
import java.awt.Dimension
import java.util.InvalidPropertiesFormatException

internal object TextRendererUtil {
    private const val ATLAS_WRONG_FORMAT_ERROR_TEXT = "Invalid texture atlas format"

    fun generateMap(
        charSize: Dimension,
        textureAtlas: Texture2D,
        characters: List<Char>
    ): HashMap<Char, Point2D> {
        val out = HashMap<Char, Point2D>()
        val xStep: Int
        val yStep: Int
        val charsCount: Int

        val widthIsValid = textureAtlas.getWidthF() % charSize.getWidth() == 0.0
        val heightIsValid = textureAtlas.getHeightF() % charSize.getHeight() == 0.0

        if (widthIsValid && heightIsValid) {
            xStep = (textureAtlas.getWidthF() / charSize.getWidth()).toInt()
            yStep = (textureAtlas.getHeightF() / charSize.getHeight()).toInt()
            charsCount = xStep * yStep
            if (charsCount != characters.size)
                throw InvalidPropertiesFormatException(
                    "$ATLAS_WRONG_FORMAT_ERROR_TEXT: " +
                            "\n Calculated number of characters = $charsCount" +
                            "\n Actual number of characters = ${characters.size}"
                )
        } else
            throw InvalidPropertiesFormatException(ATLAS_WRONG_FORMAT_ERROR_TEXT)

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