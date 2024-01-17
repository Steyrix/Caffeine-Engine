package engine.core.game_object

import engine.core.update.SetOfParameters
import engine.core.window.Window

/*
    DynamicGameEntity is designed to offer the ability to override spawning logic.
    Dynamic entities are entities that can leave the game context.
 */
abstract class DynamicGameEntity<P : SetOfParameters>(
    protected val parameters: P
) : SingleGameEntity() {

    private var isSpawned = false

    abstract fun preSpawn(setOfParameters: P)

    fun spawn(setOfParameters: P) {
        preSpawn(setOfParameters)
        isSpawned = true
    }

    open fun despawn() {
        isSpawned = false
    }

    override fun draw() {
        if (!isSpawned) return
        super.draw()
    }

    override fun update(deltaTime: Float) {
        if (!isSpawned) return
        super.update(deltaTime)
    }

    override fun input(window: Window) {
        if (!isSpawned) return
        super.input(window)
    }

    abstract fun getParams(): P
}