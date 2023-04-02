package demo.labyrinth.data

import demo.labyrinth.data.gameobject.GameObject
import demo.labyrinth.hp.HealthBar
import engine.core.entity.CompositeEntity
import engine.core.loop.AccumulatedTimeEvent
import engine.core.render.render2D.AnimatedObject2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.tiled.TileMap
import engine.feature.tiled.traversing.TileGraph
import engine.feature.tiled.traversing.TileTraverser

object Campfire : GameObject {
    override var it: CompositeEntity? = null
    var graphicalComponent: AnimatedObject2D? = null
}