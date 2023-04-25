package demo.labyrinth.data.gameobject

import engine.core.entity.CompositeEntity
import engine.core.loop.AccumulatedTimeEvent
import engine.core.scene.GameObject
import engine.core.update.SetOf2DParametersWithVelocity

class NPCs : GameObject {

    val objects: MutableList<GameObject> = mutableListOf()
    val parameters: MutableList<SetOf2DParametersWithVelocity> = mutableListOf()

    private val actions: MutableList<AccumulatedTimeEvent> = mutableListOf()

    override var it: CompositeEntity? = null

    override fun isDisposed(): Boolean {
        return false
    }

    override fun update(deltaTime: Float) {
        objects.forEach { entity ->
            entity.update(deltaTime)
            if (entity.isDisposed()) {
                actions.add(
                        AccumulatedTimeEvent(
                                timeLimit = 10f,
                                action = { this.objects.remove(entity) },
                                initialTime = 0f
                        )
                )
            }
        }

        actions.forEach { it.schedule(deltaTime) }
    }

    override fun draw() {
        objects.sortBy { it.getZLevel() }
        objects.forEach { entity ->
            entity.draw()
        }
    }

    // TODO: fix
    override fun getZLevel(): Float {
        return 10f
    }
}