package demo.labyrinth.skeleton

import engine.core.entity.BehaviouralEntity
import engine.core.entity.behavior.Behavior

import engine.core.render.render2D.AnimatedObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.geometry.Point2D
import engine.feature.tiled.traversing.TileTraverser

class Skeleton(
        private val drawableComponent: AnimatedObject2D,
        params: SetOf2DParametersWithVelocity,
        behavior: Behavior,
        private val tileTraverser: TileTraverser,
        private val playerParams: SetOf2DParametersWithVelocity
) : BehaviouralEntity(behavior, params) {

    private val controller = SkeletonController(
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
        tileTraverser.moveTo(Point2D(playerParams.x, playerParams.y))
        drawableComponent.setAnimationByKey(controller.getAnimationKey())
    }
}