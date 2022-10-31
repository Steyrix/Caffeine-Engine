package engine.feature.collision.boundingbox

import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.Collider

class BoundingBoxCollider(
        private val box: BoundingBox,
        private val parameters: SetOf2DParametersWithVelocity
) : Collider {

    private var intersectedBox: BoundingBox? = null

    override fun reactToCollision() {
        intersectedBox?.let {
            // todo remove vertical difference on horizontal collision and vice versa
            val horizontalDiff = box.getIntersectionWidth(it)
            val verticalDiff = box.getIntersectionHeight(it)
            if (isHorizontalContact()) {
                if (horizontalDiff != 0f) {
                    parameters.x += horizontalDiff + 2
                    parameters.velocityX = 0f
                }
            } else {
                if (verticalDiff != 0f) {
                    parameters.y += verticalDiff
                }
                if (parameters.velocityY != 0f) {
                    parameters.velocityY = 0f
                }
            }
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

    private fun isHorizontalContact(): Boolean {
        intersectedBox?.let {
            return it.containsAnyPointOf(false, box.getHorizontalContactPointSet())
        } ?: return false
    }
}