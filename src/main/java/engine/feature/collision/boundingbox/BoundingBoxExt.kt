package engine.feature.collision.boundingbox

import engine.core.geometry.Point2D

fun BoundingBox.getLeftCollisionPoints() = listOf(
        Point2D(x, y),
        Point2D(x, y + ySize / 4),
        Point2D(x, y + ySize / 2),
        Point2D(x, bottomY - ySize / 4),
        Point2D(x, bottomY)
)

fun BoundingBox.getRightCollisionPoints() = listOf(
        Point2D(rightX, y),
        Point2D(rightX, y + ySize / 4),
        Point2D(rightX, y + ySize / 2),
        Point2D(rightX, bottomY - ySize / 4),
        Point2D(rightX, bottomY)
)

fun BoundingBox.getTopCollisionPoints() = listOf(
        Point2D(x, y),
        Point2D(x + xSize / 4, y),
        Point2D(x + xSize / 2, y),
        Point2D(rightX - xSize / 4, y),
        Point2D(rightX, y)
)

fun BoundingBox.getBottomCollisionPoints() = listOf(
        Point2D(x, bottomY),
        Point2D(x + xSize / 4, bottomY),
        Point2D(x + xSize / 2, bottomY),
        Point2D(rightX - xSize / 4, bottomY),
        Point2D(rightX, bottomY)
)