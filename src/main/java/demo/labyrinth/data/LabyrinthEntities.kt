package demo.labyrinth.data

import engine.core.entity.BehaviouralEntity
import engine.core.entity.CompositeEntity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.collision.boundingbox.BoundingBoxCollider
import engine.feature.collision.tiled.TiledCollider
import engine.feature.tiled.TileMap

interface GameObject {
    fun update(deltaTime: Float)

    fun draw()
}

object Character : GameObject {
    var graphicalComponent: AnimatedObject2D? = null
    var boundingBox: BoundingBox? = null
    var it: CompositeEntity? = null
    var boxCollider: BoundingBoxCollider? = null
    var tiledCollider: TiledCollider? = null

    override fun update(deltaTime: Float) {
        it?.update(deltaTime)
    }

    override fun draw() {
        it?.draw()
    }
}

object Campfire : GameObject {
    var it: CompositeEntity? = null
    var graphicalComponent: AnimatedObject2D? = null

    override fun update(deltaTime: Float) {
        it?.update(deltaTime)
    }

    override fun draw() {
        it?.draw()
    }

}

object Map : GameObject {
    var it: CompositeEntity? = null
    var graphicalComponent: TileMap? = null
    var parameters: SetOfStatic2DParameters = SetOfStatic2DParameters(
            0f, 0f, 0f, 0f, 0f
    )

    override fun update(deltaTime: Float) {
        it?.update(deltaTime)

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

    override fun draw() {
        it?.draw()
    }

}

object Crate : GameObject {
    var boundingBox: BoundingBox? = null
    var it: CompositeEntity? = null
    var graphicalComponent: OpenGlObject2D? = null

    override fun update(deltaTime: Float) {
        it?.update(deltaTime)
    }

    override fun draw() {
        it?.draw()
    }

}

object Skeletons : GameObject {
    val it: MutableList<BehaviouralEntity> = mutableListOf()
    val parameters: MutableList<SetOf2DParametersWithVelocity> = mutableListOf()
    override fun update(deltaTime: Float) {
        it.forEach { entity ->
            entity.update(deltaTime)
        }
    }

    override fun draw() {
        it.forEach { entity ->
            entity.draw()
        }
    }
}