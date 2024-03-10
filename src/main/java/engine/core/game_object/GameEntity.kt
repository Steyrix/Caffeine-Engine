package engine.core.game_object

import engine.core.window.Window

/*
    GameEntity interface is responsible for placing an entity in rendering queue according
    to global z-axis value.
 */
interface GameEntity {

    var isSpawned: Boolean

    fun update(deltaTime: Float)

    fun draw()

    fun input(window: Window)

    fun isDisposed(): Boolean

    fun getZLevel(): Float

    abstract fun preSpawn(spawnOptions: SpawnOptions)

    fun spawn(spawnOptions: SpawnOptions) {
        preSpawn(spawnOptions)
        isSpawned = true
    }

    fun despawn() {
        isSpawned = false
    }
}