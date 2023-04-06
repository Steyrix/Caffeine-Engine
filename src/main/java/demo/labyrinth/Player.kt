package demo.labyrinth

import demo.labyrinth.interaction.AttackInteraction
import engine.core.entity.CompositeEntity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.interaction.Interaction

class Player(
        private val drawableComponent: AnimatedObject2D,
        params: SetOf2DParametersWithVelocity
) : CompositeEntity() {

    private val controller = SimpleController2D(
            params,
            absVelocityY = 10f,
            absVelocityX = 10f,
            modifier = 20f,
            isControlledByUser = true
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

    override fun getInteraction(): Interaction? {
        if (controller.isStriking) return AttackInteraction()
        return null
    }
}