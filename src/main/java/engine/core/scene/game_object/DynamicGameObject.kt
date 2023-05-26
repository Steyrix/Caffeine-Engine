package engine.core.scene.game_object

import engine.core.update.SetOfParameters

abstract class DynamicGameObject<P : SetOfParameters> : SingleGameObject() {

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
        if (isSpawned) {
            super.draw()
        }
    }
}