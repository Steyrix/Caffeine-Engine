package demo.labyrinth.goblin

import engine.core.entity.CompositeEntity
import engine.core.loop.AccumulatedTimeEvent
import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.getCenterPoint
import engine.feature.tiled.traversing.TileTraverser

class Goblin(
        private val drawableComponent: AnimatedObject2D,
        params: SetOf2DParametersWithVelocity,
        private val tileTraverser: TileTraverser,
        private val playerParams: SetOf2DParametersWithVelocity
) : CompositeEntity() {

    private val startChasing = AccumulatedTimeEvent(
        timeLimit = 1f
    ) { tileTraverser.moveTo(playerParams.getCenterPoint()) }

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
        startChasing.schedule(deltaTime)
        drawableComponent.setAnimationByKey(controller.getAnimationKey())
    }
}