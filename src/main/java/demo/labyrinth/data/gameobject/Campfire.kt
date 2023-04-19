package demo.labyrinth.data

import demo.labyrinth.data.gameobject.GameObject
import engine.core.entity.CompositeEntity
import engine.core.render.render2D.AnimatedObject2D

object Campfire : GameObject {
    override var it: CompositeEntity? = null
    var graphicalComponent: AnimatedObject2D? = null
}