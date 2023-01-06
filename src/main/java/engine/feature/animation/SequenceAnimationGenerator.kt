package engine.feature.animation

object SequenceAnimationGenerator {

    fun generate(
            xRange: Pair<Int, Int>,
            yRange: Pair<Int, Int>,
            frameSizeX: Float,
            frameSizeY: Float
    ): List<FrameParameters> {

        val out = mutableListOf<FrameParameters>()
        var xFirst = xRange.first
        val xLast = xRange.second
        var yFirst = yRange.first
        val yLast = yRange.second

        while (yFirst <= yLast) {

            val frameY = yFirst + 1
            val yOffset = yFirst * frameSizeY

            while (xFirst <= xLast) {
                val frameX = xFirst + 1
                val xOffset = xFirst * frameSizeX

                out.add(
                        FrameParameters(
                                xOffset,
                                yOffset,
                                frameX,
                                frameY
                        )
                )

                xFirst++
            }

            yFirst++
        }

        return out.toList()
    }
}