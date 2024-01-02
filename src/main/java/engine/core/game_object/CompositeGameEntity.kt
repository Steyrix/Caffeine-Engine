package engine.core.game_object

import engine.core.window.Window

// TODO disposing
open class CompositeGameEntity : GameEntity {

    private val objectList = mutableListOf<GameEntity>()

    override fun update(deltaTime: Float) {
        objectList.forEach {
            it.update(deltaTime)
        }
    }

    override fun draw() {
        objectList.forEach {
            it.draw()
        }
    }

    override fun input(window: Window) {
        objectList.forEach {
            it.input(window)
        }
    }

    override fun isDisposed(): Boolean {
        return false
    }

    fun addComponent(gameEntity: GameEntity) {
        objectList.add(gameEntity)
    }

    fun removeComponent(gameEntity: GameEntity) {
        objectList.remove(gameEntity)
    }

    fun getInnerObjects() = objectList.toList()
}