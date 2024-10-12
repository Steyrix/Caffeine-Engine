package engine.feature.procedural.autotiling

object Autotiler {

    fun assignTiles(
        bitValueToId: Map<Int,Int>,
        layerData: List<Int>, // list of 0s and 1s
        widthInTiles: Int,
        heightInTiles: Int
    ) {

        layerData.forEachIndexed { index, it ->
            var bitValue = 0

            var leftValue = if (index % widthInTiles > 0) {
                layerData[index - 1]
            } else {
                0
            }

            var rightValue = if (index % widthInTiles < widthInTiles) {
                layerData[index + 1]
            } else {
                0
            }

            var topValue = if (index + widthInTiles < layerData.size - 1) {
                layerData[index + widthInTiles]
            } else {
                0
            }

            var bottomValue = if(index - widthInTiles >= 0) {
                layerData[index - widthInTiles]
            } else {
                0
            }

            // TODO: calculate bit value
            // TODO: get corresponding tile
        }
    }
}