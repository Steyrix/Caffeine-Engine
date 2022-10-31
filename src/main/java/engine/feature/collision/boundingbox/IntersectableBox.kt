package engine.feature.collision.boundingbox

import engine.core.render.render2D.Drawable2D
import engine.feature.geometry.Point2D

interface IntersectableBox : Drawable2D {

    var x: Float
    var y: Float
    var xSize: Float
    var ySize: Float

    val rightX: Float
        get() = x + xSize

    val bottomY: Float
        get() = y + ySize

    fun setPosition(nX: Float, nY: Float) {
        x = nX
        y = nY
    }

    fun isIntersectingByX(anotherBox: BoundingBox) = x < anotherBox.rightX && rightX > anotherBox.x

    fun isIntersectingByY(anotherBox: BoundingBox) = y < anotherBox.bottomY && bottomY > anotherBox.y

    fun isIntersecting(anotherBox: BoundingBox) = isIntersectingByX(anotherBox) && isIntersectingByY(anotherBox)

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