package engine.core.session

import engine.core.game_object.GameEntity

abstract class Session {

    protected abstract val persistentGameEntities: MutableList<GameEntity>

    abstract fun init(presets: SessionPresets)

    abstract fun getPersistentObjects(): List<GameEntity>
}