package demo.medieval_game.data.gameobject.on_map

import engine.core.controllable.AnimationController
import engine.core.entity.Entity
import engine.core.render.AnimatedModel2D
import engine.core.update.Updatable

class ChestController(
    drawableComponent: AnimatedModel2D
) : AnimationController(drawableComponent), Entity, Updatable {

    override fun getAnimationKey(): String {
        TODO("Not yet implemented")
    }
}