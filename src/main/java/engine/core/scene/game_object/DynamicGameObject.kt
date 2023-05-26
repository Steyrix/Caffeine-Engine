package engine.core.scene.game_object

import engine.core.update.SetOfParameters

abstract class DynamicGameObject<P : SetOfParameters> : SingleGameObject() {

    private var isSpawned = false

    open fun spawn(setOfParameters: P) {
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