package demo.labyrinth.data

import demo.labyrinth.hp.HealthBar
import engine.core.entity.BehaviouralEntity
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.collision.boundingbox.BoundingBoxCollider
import engine.feature.collision.tiled.TiledCollider
import engine.feature.tiled.TileMap

interface GameObject {
    var it: CompositeEntity?
    fun update(deltaTime: Float) {
        it?.update(deltaTime)
    }

    fun draw() {
        it?.draw()
    }

    fun addComponent(entity: Entity?, params: SetOfParameters) {
        if (entity == null) return
        it?.addComponent(entity, params)
    }
}

object Character : GameObject {
    override var it: CompositeEntity? = null
    var graphicalComponent: AnimatedObject2D? = null
    var boundingBox: BoundingBox? = null
    var hp: HealthBar? = null
    var boxCollider: BoundingBoxCollider? = null
    var tiledCollider: TiledCollider? = null
}

object Campfire : GameObject {
    override var it: CompositeEntity? = null
    var graphicalComponent: AnimatedObject2D? = null
}

object Map : GameObject {
    override var it: CompositeEntity? = null
    var graphicalComponent: TileMap? = null
    var parameters: SetOfStatic2DParameters = SetOfStatic2DParameters(
            0f, 0f, 0f, 0f, 0f
    )

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        accumulated += deltaTime
        if (accumulated >= timeLimit) {
            accumulated = 0f
            if (current + 1 >= lightIntensityCaps.size) {
                current = 0
            } else current++
            graphicalComponent?.shader?.let {
                it.bind()
                it.setUniform("lightIntensityCap", lightIntensityCaps[current])
            }
        }
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

    override fun update(deltaTime: Float) {
        if (!isHittable) accumulatedTime += deltaTime
        if (accumulatedTime >= hitCooldownTime) {
            accumulatedTime = 0f
            isHittable = true
        }
        super.update(deltaTime)
    }
}

object Skeletons {
    val it: MutableList<BehaviouralEntity> = mutableListOf()
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