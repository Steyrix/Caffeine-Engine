package engine.feature.procedural.autotiling

object Autotiler {

    fun assignTiles(
        bitValueToId: Map<Int,Int>,
        layerData: List<Int>,
        widthInTiles: Int
    ): List<Int> {

        val out = mutableListOf<Int>()

        val binaryData = layerData.map {
            if (it != 0) {
                1
            } else { 0 }
        }

        layerData.forEachIndexed { index, _ ->
            val bitValue = calculateBitMasks(index, widthInTiles, binaryData)
            out.add(bitValueToId[bitValue] ?: -1)
        }

        return out
    }

    private fun calculateBitMasks(
        index: Int,
        widthInTiles: Int,
        binaryData: List<Int>
    ): Int {
        val leftTopCornerValue = if (index % widthInTiles > 0 && index / widthInTiles > 0) {
            binaryData[index - widthInTiles - 1]
        } else { 0 } * 1

        val topValue = if (index - widthInTiles >= 0) {
            binaryData[index - widthInTiles]
        } else { 0 } * 2

        val rightTopCornerValue = if (index % widthInTiles < widthInTiles && index / widthInTiles > 0) {
            binaryData[index - widthInTiles + 1]
        } else { 0 } * 4

        val leftValue = if (index % widthInTiles > 0) {
            binaryData[index - 1]
        } else { 0 } * 8

        val rightValue = if (index % widthInTiles < widthInTiles) {
            binaryData[index + 1]
        } else { 0 } * 16

        val leftBottomCornerValue = if (index + widthInTiles <= binaryData.size - 1 && index % widthInTiles > 0) {
            binaryData[index + widthInTiles - 1]
        } else { 0 } * 32

        val bottomValue = if(index + widthInTiles <= binaryData.size - 1) {
            binaryData[index + widthInTiles]
        } else { 0 } * 64

        val rightBottomCornerValue = if (index + widthInTiles < binaryData.size - 1 && index % widthInTiles < widthInTiles
        ) {
            binaryData[index + widthInTiles + 1]
        } else { 0 } * 128

        return leftTopCornerValue
            .plus(topValue)
            .plus(rightTopCornerValue)
            .plus(leftValue)
            .plus(rightValue)
            .plus(leftBottomCornerValue)
            .plus(bottomValue)
            .plus(rightBottomCornerValue)
    }
}