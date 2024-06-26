package engine.feature.collision.boundingbox

import engine.core.geometry.Point2D
import kotlin.math.abs

interface IntersectableBox {

    var x: Float
    var y: Float
    var xOffset: Float
    var yOffset: Float
    var xSize: Float
    var ySize: Float

    val rightX: Float
        get() = x + xOffset + xSize

    val bottomY: Float
        get() = y + yOffset + ySize

    val startX: Float
        get() = x + xOffset

    val startY: Float
        get() = y + yOffset

    fun setPosition(nX: Float, nY: Float) {
        x = nX
        y = nY
    }

    fun isIntersectingByX(anotherBox: BoundingBox) =
        abs((startX + xSize / 2) - (anotherBox.startX + anotherBox.xSize / 2)) * 2 < (xSize + anotherBox.xSize)

    fun isIntersectingByY(anotherBox: BoundingBox) =
        abs((startY + ySize / 2) - (anotherBox.startY + anotherBox.ySize / 2)) * 2 < (ySize + anotherBox.ySize)

    fun isIntersecting(anotherBox: BoundingBox) = isIntersectingByX(anotherBox) && isIntersectingByY(anotherBox)

    fun isReachingCenterOf(anotherBox: BoundingBox): Boolean {
        val center = anotherBox.getCenterPoint()
        return center.x in x..(x + xSize) && center.y in y..(y + ySize)
    }

    fun isContainingEveryOf(points: List<Point2D>): Boolean {
        points.forEach {
            val doesNotContain = it.x > rightX
                    || it.x < x
                    || it.y > bottomY
                    || it.y < y
            if (doesNotContain) {
                return false
            }

        }
        return true
    }

    fun isContainingNumberOf(numberOfPoints: Int, isStrict: Boolean, points: List<Point2D>): Boolean {
        var cnt = 0
        points.forEach {
            if (cnt == numberOfPoints) return true
            if (isPointInBox(it, isStrict)) cnt++
        }

        return cnt >= numberOfPoints
    }

    fun isContainingOneOf(isStrict: Boolean, points: List<Point2D>): Boolean {
        points.forEach {
            if (isPointInBox(it, isStrict)) return true
        }

        return false
    }

    fun getIntersectionWidth(anotherBox: BoundingBox): Float {
        return if (anotherBox.x >= x) {
            -(rightX - anotherBox.x)
        } else {
            anotherBox.rightX - x
        }
    }

    fun getIntersectionHeight(anotherBox: BoundingBox): Float {
        return if (anotherBox.y >= y) {
            -(bottomY - anotherBox.y)
        } else {
            anotherBox.bottomY - y
        }
    }

    fun getCenterPoint(): Point2D {
        return Point2D(x + xSize / 2, y + ySize / 2)
    }

    private fun isPointInBox(point: Point2D, isStrict: Boolean): Boolean {
        return if (isStrict) {
            point.x < rightX
                    && point.x > x
                    && point.y < bottomY
                    && point.y > y
        } else {
            point.x in x..rightX
                    && point.y <= bottomY
                    && point.y >= y
        }
    }
}