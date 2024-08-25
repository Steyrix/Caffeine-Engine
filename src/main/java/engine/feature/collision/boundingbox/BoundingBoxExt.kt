package engine.feature.collision.boundingbox

import engine.core.geometry.Point2D

fun BoundingBox.getLeftCollisionPoints() = listOf(
    Point2D(x, y),
    Point2D(x, y + ySize / 4),
    Point2D(x, y + ySize / 2),
    Point2D(x, bottom() - ySize / 4),
    Point2D(x, bottom())
)

fun BoundingBox.getRightCollisionPoints() = listOf(
    Point2D(end(), y),
    Point2D(end(), y + ySize / 4),
    Point2D(end(), y + ySize / 2),
    Point2D(end(), bottom() - ySize / 4),
    Point2D(end(), bottom())
)

fun BoundingBox.getTopCollisionPoints() = listOf(
    Point2D(x, y),
    Point2D(x + xSize / 4, y),
    Point2D(x + xSize / 2, y),
    Point2D(end() - xSize / 4, y),
    Point2D(end(), y)
)

fun BoundingBox.getBottomCollisionPoints() = listOf(
    Point2D(x, bottom()),
    Point2D(x + xSize / 4, bottom()),
    Point2D(x + xSize / 2, bottom()),
    Point2D(end() - xSize / 4, bottom()),
    Point2D(end(), bottom())
)