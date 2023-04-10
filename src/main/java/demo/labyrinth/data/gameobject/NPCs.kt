package demo.labyrinth.data.gameobject

import engine.core.loop.AccumulatedTimeEvent
import engine.core.update.SetOf2DParametersWithVelocity

object NPCs {
    val it: MutableList<GameObject> = mutableListOf()
    val parameters: MutableList<SetOf2DParametersWithVelocity> = mutableListOf()

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    fun update(deltaTime: Float) {
        it.forEach { entity ->
            entity.update(deltaTime)
            if (entity.isDisposed()) {
                actions.add(
                        AccumulatedTimeEvent(
                                timeLimit = 10f,
                                action = { this.it.remove(entity) },
                                initialTime = 0f
                        )
                )
            }
        }

        actions.forEach { it.schedule(deltaTime) }
    }

    fun draw() {
        it.forEach { entity ->
            entity.draw()
        }
    }
}