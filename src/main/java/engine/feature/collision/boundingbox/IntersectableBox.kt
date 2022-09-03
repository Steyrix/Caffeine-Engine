package engine.feature.collision.boundingbox

import engine.core.render.render2D.Drawable2D
import engine.feature.geometry.Point2D

interface IntersectableBox : Drawable2D {

    val rightX: Float
        get() = x + xSize

    val bottomY: Float
        get() = y + ySize

    fun setPosition(nX: Float, nY: Float) {
        x = nX
        y = nY
    }

    fun intersectsX(anotherBox: BoundingBox) = x < anotherBox.rightX || rightX > anotherBox.x


    fun intersectsY(anotherBox: BoundingBox) = y < anotherBox.bottomY || bottomY > anotherBox.y

    fun intersects(anotherBox: BoundingBox) = intersectsX(anotherBox) && intersectsY(anotherBox)

    fun containsEveryPointOf(vararg points: Point2D): Boolean {
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

    fun containsNumberOfPoints(numberOfPoints: Int, strict: Boolean, points: List<Point2D>): Boolean {
        if (numberOfPoints <= 0) {
            return true
        }

        var cnt = 0
        points.forEach {
            val strictCondition = it.x < rightX
                    && it.x > x
                    && it.y < bottomY
                    && it.y > y

            val nonStrictCondition = it.x in x..rightX
                    && it.y <= bottomY
                    && it.y >= y

            if (strict) {
                if (strictCondition) {
                    cnt++
                }
            } else {
                if (nonStrictCondition) {
                    cnt++
                }
            }
        }

        return cnt >= numberOfPoints
    }

    fun containsAnyPointOf(strict: Boolean, points: List<Point2D>): Boolean {
        points.forEach {
            val strictCondition = it.x < rightX
                    && it.x > x
                    && it.y < bottomY
                    && it.y > y

            val nonStrictCondition = it.x in x..rightX
                    && it.y <= bottomY
                    && it.y >= y

            if (strict) {
                if (strictCondition) {
                    return true
                }
            } else {
                if (nonStrictCondition) {
                    return true
                }
            }
        }

        return false
    }

    fun containsPoint(strict: Boolean, pointFS: ArrayList<Point2D>): Boolean {
        return containsAnyPointOf(strict, pointFS)
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
}