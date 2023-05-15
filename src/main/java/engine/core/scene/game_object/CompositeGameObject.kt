package engine.core.scene.game_object

import engine.core.window.Window

open class CompositeGameObject : GameObject {

    private val it = mutableListOf<GameObject>()

    var descendingZ = false

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

    override fun getZLevel(): Float {
        val out = if (descendingZ) {
            it.maxOf { it.getZLevel() }
        } else {
            it.minOf { it.getZLevel() }
        }

        return if (out.isNaN()) {
            Float.NEGATIVE_INFINITY
        } else {
            out
        }
    }

    fun getInnerObjects() = it.toList()
}