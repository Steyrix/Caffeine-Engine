package engine.feature.collision.boundingbox

import engine.core.update.SetOf2DParametersWithVelocity

val emptyBehavior = {
    _: BoundingBox, _: BoundingBox, _: SetOf2DParametersWithVelocity ->
}

val defaultBehavior = {
    intersectedBox: BoundingBox,
    box: BoundingBox,
    parameters: SetOf2DParametersWithVelocity ->

    intersectedBox.let {
        val horizontalDiff = box.getIntersectionWidth(it)
        val verticalDiff = box.getIntersectionHeight(it)

        if (isHorizontalContact(it, box)) {
            if (horizontalDiff != 0f) {
                parameters.x += horizontalDiff + 2
                parameters.velocityX = 0f
            }
        } else if (isVerticalContact(it, box)) {
            if (verticalDiff != 0f) {
                parameters.y += verticalDiff + 2
                parameters.velocityY = 0f
            }
        }
    }
}

private fun isHorizontalContact(intersectedBox: BoundingBox, box: BoundingBox): Boolean {
    val numberForContact = 2
    intersectedBox.let {
        return it.isContainingNumberOf(numberForContact, false, box.getLeftCollisionPoints())
                || it.isContainingNumberOf(numberForContact, false, box.getRightCollisionPoints())
    }
}

private fun isVerticalContact(intersectedBox: BoundingBox, box: BoundingBox): Boolean {
    val numberForContact = 2
    intersectedBox.let {
        return it.isContainingNumberOf(numberForContact, false, box.getTopCollisionPoints())
                || it.isContainingNumberOf(numberForContact, false, box.getBottomCollisionPoints())
    }
}