package demo.labyrinth.data.gameobject

import demo.labyrinth.data.campfireParameters
import engine.core.entity.CompositeEntity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.scene.GameObject

object Campfire : GameObject {
    override var it: CompositeEntity? = null
    var graphicalComponent: AnimatedObject2D? = null

    override fun getZLevel(): Float {
        return campfireParameters.y
    }
}