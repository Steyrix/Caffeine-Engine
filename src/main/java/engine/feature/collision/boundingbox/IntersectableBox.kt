package engine.feature.collision.boundingbox

import engine.core.geometry.Point2D

interface IntersectableBox {

    var x: Float
    var y: Float
    var xOffset: Float
    var yOffset: Float
    var xSize: Float
    var ySize: Float

    fun start(): Float {
        return x + xOffset
    }

    fun end(): Float {
        return x + xOffset + xSize
    }

    fun bottom(): Float {
        return y + yOffset + ySize
    }

    fun top(): Float {
        return y + yOffset
    }

    fun setPosition(nX: Float, nY: Float) {
        x = nX
        y = nY
    }

    fun isIntersectingByX(anotherBox: BoundingBox): Boolean {
        return end() >= anotherBox.start() && anotherBox.end() >= start()
    }

    fun isIntersectingByY(anotherBox: BoundingBox): Boolean {
        return bottom() >= anotherBox.top() && anotherBox.bottom() >= top()
    }

    fun isIntersecting(anotherBox: BoundingBox) = isIntersectingByX(anotherBox) && isIntersectingByY(anotherBox)

    fun isReachingCenterOf(anotherBox: BoundingBox): Boolean {
        val center = anotherBox.getCenterPoint()
        return center.x in x..(x + xSize) && center.y in y..(y + ySize)
    }

    fun isContainingEveryOf(points: List<Point2D>): Boolean {
        points.forEach {
            val doesNotContain = it.x > end()
                    || it.x < x
                    || it.y > bottom()
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
            -(end() - anotherBox.x)
        } else {
            anotherBox.end() - x
        }
    }

    fun getIntersectionHeight(anotherBox: BoundingBox): Float {
        return if (anotherBox.top() >= top()) {
            -(bottom() - anotherBox.start())
        } else {
            anotherBox.bottom() - top()
        }
    }

    fun getCenterPoint(): Point2D {
        return Point2D(x + xSize / 2, y + ySize / 2)
    }

    private fun isPointInBox(point: Point2D, isStrict: Boolean): Boolean {
        return if (isStrict) {
            point.x < end()
                    && point.x > x
                    && point.y < bottom()
                    && point.y > y
        } else {
            point.x in x..end()
                    && point.y <= bottom()
                    && point.y >= y
        }
    }
}