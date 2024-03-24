package engine.core.game_object

import engine.core.window.Window

open class CompositeGameEntity : GameEntity {

    private val objectList = mutableListOf<GameEntity>()

    override var isSpawned: Boolean = false

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

    fun addComponent(gameEntity: GameEntity) {
        objectList.add(gameEntity)
    }

    fun removeComponent(gameEntity: GameEntity) {
        objectList.remove(gameEntity)
    }

    override fun getZLevel(): Float {
        if (objectList.isEmpty()) return Float.NEGATIVE_INFINITY
        return objectList.maxOf { it.getZLevel() }
    }

    override fun preSpawn(spawnOptions: SpawnOptions) {
        TODO("Not yet implemented")
    }

    fun getInnerObjects() = objectList.toList()
}