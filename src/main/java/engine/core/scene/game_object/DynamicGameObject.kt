package engine.core.scene.game_object

import engine.core.update.SetOfParameters

abstract class DynamicGameObject : SingleGameObject() {

    private var isSpawned = false

    abstract fun spawn(setOfParameters: SetOfParameters)

    override fun draw() {
        if (isSpawned) {
            super.draw()
        }
    }
}