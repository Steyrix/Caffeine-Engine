package engine.core.scene.game_object

import engine.core.window.Window

// TODO different z-level for different elements, among other components in game context
// TODO disposing
open class CompositeGameObject : GameObject {

    private val objectList = mutableListOf<GameObject>()

    override fun update(deltaTime: Float) {
        objectList.forEach {
            it.update(deltaTime)
        }
    }

    override fun draw() {
        objectList.sortBy { it.getZLevel() }
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

    fun addComponent(gameObject: GameObject) {
        objectList.add(gameObject)
    }

    fun removeComponent(gameObject: GameObject) {
        objectList.remove(gameObject)
    }

    override fun getZLevel(): Float {
        if (objectList.isEmpty()) return Float.NEGATIVE_INFINITY
        return objectList.maxOf { it.getZLevel() }
    }

    fun getInnerObjects() = objectList.toList()
}