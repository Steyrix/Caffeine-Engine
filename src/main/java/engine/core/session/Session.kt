package engine.core.session

import engine.core.scene.GameObject

abstract class Session {

    protected abstract val persistentGameObjects: MutableList<GameObject>

    abstract fun init(presets: SessionPresets)

    abstract fun getPersistentObjects(): List<GameObject>
}