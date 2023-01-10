package demo.labyrinth.skeleton

import engine.core.controllable.SimpleController2D
import engine.core.entity.BehaviouralEntity
import engine.core.entity.behavior.Behavior

import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity

class Skeleton(
        private val drawableComponent: AnimatedObject2D,
        params: SetOf2DParametersWithVelocity,
        behavior: Behavior
) : BehaviouralEntity(behavior, params) {

    private val controller = SimpleController2D(
            params,
            absVelocityY = 10f,
            absVelocityX = 10f,
            modifier = 20f,
            isControlledByUser = false
    )

    init {
        addComponent(
                component = drawableComponent,
                parameters = params
        )

        addComponent(
                component = controller,
                parameters = params
        )
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        drawableComponent.setAnimationByKey(controller.getAnimationKey())
    }
}