package engine.feature.collision.boundingbox

import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.Collider
import engine.feature.collision.CollisionContext

class BoundingBoxCollider(
        private val box: BoundingBox,
        private val parameters: SetOf2DParametersWithVelocity,
        override var collisionContext: CollisionContext,
        private val onCollision: (BoundingBox) -> Unit = {}
) : Collider {

    private var intersectedBox: BoundingBox? = null

    override fun reactToCollision() {
        intersectedBox?.let {
            val horizontalDiff = box.getIntersectionWidth(it)
            val verticalDiff = box.getIntersectionHeight(it)

            if (isHorizontalContact()) {
                if (horizontalDiff != 0f) {
                    parameters.x += horizontalDiff + 2
                    parameters.velocityX = 0f
                }
            } else if (isVerticalContact()) {
                if (verticalDiff != 0f) {
                    parameters.y += verticalDiff + 2
                    parameters.velocityY = 0f
                }
            }
        }

        intersectedBox = null
    }

    override fun isColliding(entity: Entity): Boolean {
        if (entity as? BoundingBox == box) return false

        (entity as? BoundingBox)?.let {
            if (box.isIntersecting(it)) {
                intersectedBox = it
                onCollision(it)
                return true
            }
        }

        return false
    }

    private fun isHorizontalContact(): Boolean {
        val numberForContact = 2
        intersectedBox?.let {
            return it.isContainingNumberOf(numberForContact, false, box.getLeftCollisionPoints())
                    || it.isContainingNumberOf(numberForContact, false, box.getRightCollisionPoints())
        } ?: return false
    }

    private fun isVerticalContact(): Boolean {
        val numberForContact = 2
        intersectedBox?.let {
            return it.isContainingNumberOf(numberForContact, false, box.getTopCollisionPoints())
                    || it.isContainingNumberOf(numberForContact, false, box.getBottomCollisionPoints())
        } ?: return false
    }
}