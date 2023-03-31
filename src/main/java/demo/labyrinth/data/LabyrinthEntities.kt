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

object GameMap : GameObject {
    override var it: CompositeEntity? = null
    var graphicalComponent: TileMap? = null
    set(value) {
        value?.let {
            tileGraph = TileGraph(
                    value.getGraph(
                            listOf("walking_layer", "walkable_objects_layer")
                    )
            )

            field = value
        }
    }

    var parameters: SetOfStatic2DParameters = SetOfStatic2DParameters(
            0f, 0f, 0f, 0f, 0f
    )
    var tileGraph: TileGraph? = null

    private val lightBlinking = AccumulatedTimeEvent(
            timeLimit = lightBlinkingTimeLimit
    ) {
        if (current + 1 >= lightIntensityCaps.size) {
            current = 0
        } else current++
        graphicalComponent?.shader?.let {
            it.bind()
            it.setUniform("lightIntensityCap", lightIntensityCaps[current])
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        lightBlinking.schedule(deltaTime)
    }
}

object Crate : GameObject {
    override var it: CompositeEntity? = null
    var boundingBox: BoundingBox? = null
    var graphicalComponent: OpenGlObject2D? = null
    var hp: HealthBar? = null
    var isAlive = true

    private var isHittable = true
    private const val hitCooldownTime = 0.3f
    private var accumulatedTime = 0f

    fun takeDamage() {
        if (!isHittable) return
        isHittable = false
        hp?.let {
            it.filled -= 0.1f

            if (it.filled <= 0f) {
                isAlive = false
            }
        }
    }

    override fun draw() {
        if (!isAlive) return
        super.draw()
    }

    // TODO move out of collision context
    override fun update(deltaTime: Float) {
        if (!isAlive) return
        if (!isHittable) accumulatedTime += deltaTime
        if (accumulatedTime >= hitCooldownTime) {
            accumulatedTime = 0f
            isHittable = true
        }
        super.update(deltaTime)
    }
}

object Goblins {
    val it: MutableList<CompositeEntity> = mutableListOf()
    val parameters: MutableList<SetOf2DParametersWithVelocity> = mutableListOf()

    fun update(deltaTime: Float) {
        it.forEach { entity ->
            entity.update(deltaTime)
        }
    }

    fun draw() {
        it.forEach { entity ->
            entity.draw()
        }
    }
}

fun createTileTraverser(params: SetOf2DParametersWithVelocity): TileTraverser {
    return TileTraverser(
            GameMap.tileGraph!!,
            GameMap.graphicalComponent!!,
            params
    )
}