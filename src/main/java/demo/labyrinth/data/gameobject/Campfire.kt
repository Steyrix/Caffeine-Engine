package demo.labyrinth.data.gameobject

import engine.core.entity.CompositeEntity
import engine.core.render.render2D.AnimatedObject2D

object Campfire : GameObject {
    override var it: CompositeEntity? = null
    var graphicalComponent: AnimatedObject2D? = null
}