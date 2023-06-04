package engine.core.game_object

import engine.core.update.SetOfParameters
import engine.core.window.Window

abstract class DynamicGameObject<P : SetOfParameters>(
        protected val parameters: P
) : SingleGameObject() {

    private var isSpawned = false

    abstract fun preSpawn(setOfParameters: P)

    fun spawn(setOfParameters: P) {
        preSpawn(setOfParameters)
        isSpawned = true
    }

    open fun despawn() {
        isSpawned = false
    }

    final override fun draw() {
        if (!isSpawned) return

        super.draw()
    }

    final override fun update(deltaTime: Float) {
        if (!isSpawned) return

        super.update(deltaTime)
    }

    final override fun input(window: Window) {
        if (!isSpawned) return

        super.input(window)
    }

    abstract fun getParams(): P
}