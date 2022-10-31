package engine.feature.collision.boundingbox

import engine.feature.geometry.Point2D

fun BoundingBox.getCollidingPointsSet() =
        arrayListOf(
                Point2D(x, y),
                Point2D(x, bottomY),
                Point2D(rightX, bottomY),
                Point2D(rightX, y),
                Point2D(x + xSize / 2, y),
                Point2D(x + xSize / 2, bottomY),
                Point2D(x, y + ySize / 2),
                Point2D(rightX, y + ySize / 2),
                Point2D(x, y + ySize / 4),
                Point2D(x, bottomY - ySize / 4),
                Point2D(rightX, y + ySize / 4),
                Point2D(rightX, bottomY - ySize / 5),
                Point2D(x, y + ySize / 3),
                Point2D(rightX, y + ySize / 3),
                Point2D(x, bottomY - ySize / 3),
                Point2D(rightX, bottomY - ySize / 3)
        )

fun BoundingBox.getHorizontalContactPointSet() = listOf(
        Point2D(x, y + ySize / 2),
        Point2D(rightX, y + ySize / 2)
)