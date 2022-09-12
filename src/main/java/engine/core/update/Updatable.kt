package engine.core.update

interface Updatable {
    fun update(deltaTime: Float) = Unit
}