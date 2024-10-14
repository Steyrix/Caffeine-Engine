package engine.feature.procedural.autotiling

object Autotiler {

    fun assignTiles(
        bitValueToId: Map<Int,Int>,
        layerData: List<Int>,
        widthInTiles: Int
    ): List<Int> {

        val out = mutableListOf<Int>()

        val binaryData = layerData.map {
            if (it != -1) {
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

        val bottomValue = if(bottomExists(index,  widthInTiles, binaryData.size)) {
            binaryData[index + widthInTiles]
        } else { 0 } * 64

        val topValue = if (topExists(index, widthInTiles)) {
            binaryData[index - widthInTiles]
        } else { 0 } * 2

        val leftValue = if (leftExists(index, widthInTiles)) {
            binaryData[index - 1]
        } else { 0 } * 8

        val rightValue = if (rightExists(index, widthInTiles)) {
            binaryData[index + 1]
        } else { 0 } * 16

        val leftTopCornerValue = if (
            leftTopExists(index, widthInTiles, leftValue, topValue)
        ) {
            binaryData[index - widthInTiles - 1]
        } else { 0 } * 1

        val leftBottomCornerValue = if (
            leftBottomExists(index, widthInTiles, binaryData.size, leftValue, bottomValue)
        ) {
            binaryData[index + widthInTiles - 1]
        } else { 0 } * 32

        val rightTopCornerValue = if (
            rightTopExists(index, widthInTiles, topValue, rightValue)
        ) {
            binaryData[index - widthInTiles + 1]
        } else { 0 } * 4

        val rightBottomCornerValue = if (
            rightBottomExists(index, widthInTiles, binaryData.size, rightValue, bottomValue)
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

    private fun bottomExists(
        index: Int,
        widthInTiles: Int,
        size: Int
    ): Boolean = index + widthInTiles <= size - 1

    private fun topExists(
        index: Int,
        widthInTiles: Int
    ): Boolean = index - widthInTiles >= 0

    private fun leftExists(
        index: Int,
        widthInTiles: Int
    ): Boolean = index % widthInTiles > 0

    private fun rightExists(
        index: Int,
        widthInTiles: Int
    ): Boolean = index % widthInTiles < widthInTiles

    private fun leftTopExists(
        index: Int,
        widthInTiles: Int,
        leftValue: Int,
        topValue: Int
    ): Boolean = index % widthInTiles > 0
            && index / widthInTiles > 0
            && leftValue != 0 || topValue != 0

    private fun leftBottomExists(
        index: Int,
        widthInTiles: Int,
        size: Int,
        leftValue: Int,
        bottomValue: Int
    ): Boolean = index + widthInTiles <= size - 1
            && index % widthInTiles > 0
            && bottomValue != 0 || leftValue != 0

    private fun rightTopExists(
        index: Int,
        widthInTiles: Int,
        topValue: Int,
        rightValue: Int
    ): Boolean = index % widthInTiles < widthInTiles
            && index / widthInTiles > 0
            && topValue != 0 || rightValue != 0

    private fun rightBottomExists(
        index: Int,
        widthInTiles: Int,
        size: Int,
        rightValue: Int,
        bottomValue: Int
    ): Boolean = index + widthInTiles < size - 1
            && index % widthInTiles < widthInTiles
            && bottomValue != 0 || rightValue != 0
}