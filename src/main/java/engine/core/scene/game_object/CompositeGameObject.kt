package engine.core.scene.game_object

import engine.core.window.Window

class CompositeGameObject : GameObject {

    private val it = mutableListOf<GameObject>()

    override fun update(deltaTime: Float) {
        it.forEach {
            it.update(deltaTime)
        }
    }

    override fun draw() {
        it.sortBy { it.getZLevel() }
        it.forEach {
            it.draw()
        }
    }

    override fun input(window: Window) {
        it.forEach {
            it.input(window)
        }
    }

    override fun isDisposed(): Boolean {
        var isDisposed = true
        it.forEach {
            if (!it.isDisposed()) isDisposed = false
        }

        return it.isEmpty() && isDisposed
    }

    fun addComponent(gameObject: GameObject) {
        it.add(gameObject)
    }

    override fun getZLevel(): Float = it.maxOf { it.getZLevel() }

    fun getInnerObjects() = it.toList()
}