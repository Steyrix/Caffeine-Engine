package engine.feature.tiled.data.lighting

import engine.core.geometry.Point2D
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileMap
import org.joml.Vector2f

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

            val tileVectors = tilePositions.map { Vector2f(it.x, it.y) }
            val lightsVectors = lightSources.map { Vector2f(it.x, it.y) }

            tileVectors.forEach { tile ->
                lightsVectors.forEach { lightSource ->
                    val distance = tile.distance(lightSource)
                }
            }
        }
    }


}