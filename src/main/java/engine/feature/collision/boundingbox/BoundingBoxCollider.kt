package engine.feature.collision.boundingbox

import engine.core.entity.Entity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfParameters
import engine.feature.collision.Collider
import engine.feature.collision.CollisionContext

class BoundingBoxCollider(
    override val holderEntity: Entity,
    private val box: BoundingBox,
    private val parameters: SetOfParameters,
    private val onCollision: (BoundingBox, BoundingBox, SetOfParameters) -> Unit,
    private val onCollisionExitFunc: () -> Unit = {}
) : Collider {

    private var intersectedBox: BoundingBox? = null

    override fun reactToCollision() {
        intersectedBox?.let {
            onCollision(it, box, parameters)
        }
    }

    override fun onCollisionExit() {
        onCollisionExitFunc.invoke()
    }

    override fun isColliding(entity: Entity): Boolean {
        if (entity as? BoundingBox == box) return false

        (entity as? BoundingBox)?.let {
            if (box.isIntersecting(it)) {
                intersectedBox = it
                return true
            }
        }

        return false
    }
}