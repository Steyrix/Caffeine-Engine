package demo.labyrinth.data.gameobject

import engine.core.update.SetOf2DParametersWithVelocity

object NPCs {
    val it: MutableList<GameObject> = mutableListOf()
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