package engine.feature.animation

object FrameParametersGenerator {

    fun generate(
        xRange: Pair<Int, Int>,
        yRange: Pair<Int, Int>,
        atlasData: AtlasData
    ): List<FrameParameters> {
        return generate(
            xRange,
            yRange,
            atlasData.frameWidth,
            atlasData.frameHeight
        )
    }

    fun generate(
        xRange: Pair<Int, Int>,
        yRange: Pair<Int, Int>,
        frameWidth: Float,
        frameHeight: Float
    ): List<FrameParameters> {

        val out = mutableListOf<FrameParameters>()
        var xFirst = xRange.first
        val initialX = xFirst
        val xLast = xRange.second
        var yFirst = yRange.first
        val yLast = yRange.second

        val yAscending = yFirst <= yLast
        val xAscending = xFirst <= xLast

        val yConditionFunc: () -> Boolean = if (yFirst <= yLast) {
            { yFirst <= yLast }
        } else {
            { yFirst >= yLast }
        }

        val xConditionFunc: () -> Boolean = if (xFirst <= xLast) {
            { xFirst <= xLast }
        } else {
            { xFirst >= xLast }
        }

        while (yConditionFunc.invoke()) {
            val frameY = yFirst + 1
            val yOffset = yFirst * frameHeight

            xFirst = initialX
            while (xConditionFunc.invoke()) {
                val frameX = xFirst + 1
                val xOffset = xFirst * frameWidth

                out.add(
                    FrameParameters(
                        xOffset,
                        yOffset,
                        frameX,
                        frameY
                    )
                )

                if (xAscending) xFirst++ else xFirst--
            }

            if (yAscending) yFirst++ else yFirst--
        }

        return out.toList()
    }
}