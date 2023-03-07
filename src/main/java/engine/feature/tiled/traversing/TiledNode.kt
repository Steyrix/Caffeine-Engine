package engine.feature.tiled.traversing

import java.awt.geom.Point2D

data class TiledNode(
        val id: Int,
        val startPos: Point2D,
        val size: Pair<Float, Float>,
)