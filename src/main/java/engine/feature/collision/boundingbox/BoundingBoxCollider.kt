package engine.feature.collision.boundingbox

import engine.core.entity.Entity
import engine.core.update.SetOf2DParameters
import engine.feature.collision.Collider

class BoundingBoxCollider(
        private val box: BoundingBox,
        private val parameters: SetOf2DParameters
) : Collider {

    private var intersectedBox: BoundingBox? = null

    override fun reactToCollision() {
        intersectedBox?.let {
            val horizontalDiff = box.getIntersectionWidth(it)
            val verticalDiff = box.getIntersectionHeight(it)
            parameters.x -= horizontalDiff
            parameters.y -= verticalDiff
        }

        intersectedBox = null
    }

    override fun isColliding(entity: Entity): Boolean {
        (entity as? BoundingBox)?.let {
            if (box.intersects(it)) {
                intersectedBox = it
                return true
            }
        }

        return false
    }
}