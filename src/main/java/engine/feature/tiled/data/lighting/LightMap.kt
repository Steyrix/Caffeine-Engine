package engine.feature.tiled.data.lighting

import engine.core.geometry.Point2D
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileMap

class LightMap(
    lightSources: List<SetOfStatic2DParameters>
) {

    companion object {
        fun generateInstance(
            tileMap: TileMap,
            lightSources: List<SetOfStatic2DParameters>
        ) {
            val outLightIntensityList = mutableListOf<Float>()

            val tilesCount = tileMap.tilesCount
            val tilePositions = mutableListOf<Point2D>()

            for (index in 0 until tilesCount) {
                tilePositions.add(
                    tileMap.getTilePosition(index)
                )
            }

            tilePositions.forEach {

            }
        }
    }


}