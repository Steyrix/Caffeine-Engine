package demo.labyrinth

import engine.core.controllable.SimpleController2D
import engine.core.entity.CompositeEntity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity

class Player(
        drawableComponent: AnimatedObject2D,
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

        components
                .filter { it.key is AnimatedObject2D }
                .forEach {
                    (it.key as AnimatedObject2D).apply {
                        setAnimationByKey(controller.getAnimationKey())
                        setArrayTextureLayer(controller.getTextureArrayLayer())
                    }
                }
    }
}