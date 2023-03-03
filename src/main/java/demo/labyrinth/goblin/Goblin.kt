package demo.labyrinth.goblin

import engine.core.entity.CompositeEntity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.geometry.Point2D
import engine.feature.tiled.traversing.TileTraverser

class Goblin(
        private val drawableComponent: AnimatedObject2D,
        params: SetOf2DParametersWithVelocity,
        private val tileTraverser: TileTraverser,
        private val playerParams: SetOf2DParametersWithVelocity
) : CompositeEntity() {

    private val chaseTimeLimit = 1f
    private var accumulatedTime = 0f

    private val controller = GoblinController(
            params,
            modifier = 20f,
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

        addComponent(
                component = tileTraverser,
                parameters = params
        )
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        accumulatedTime += deltaTime
        if (accumulatedTime >= chaseTimeLimit) {
            tileTraverser.moveTo(
                    Point2D(
                            playerParams.x + playerParams.xSize / 2,
                            playerParams.y + playerParams.ySize / 2
                    )
            )
            accumulatedTime = 0f
        }

        drawableComponent.setAnimationByKey(controller.getAnimationKey())
    }
}