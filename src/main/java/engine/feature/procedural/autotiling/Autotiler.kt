package engine.feature.procedural.autotiling

object Autotiler {

    fun generateAutoTiledArea(
        bitValueToId: Map<Int,Int>,
        layerData: List<Int>,
        widthInTiles: Int,
        heightInTiles: Int
    ) {

        layerData.forEachIndexed { index, it ->
            var bitValue = 0
            // TODO: calculate bit value
            // TODO: get corresponding tile
        }
    }
}