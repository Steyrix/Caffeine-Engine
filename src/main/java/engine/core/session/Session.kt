package engine.core.session

import engine.core.game_object.GameEntity
import engine.feature.matrix.MatrixState

abstract class Session {

    protected abstract val persistentGameEntities: MutableList<GameEntity>

    abstract val matrixState: MatrixState

    abstract fun init(presets: SessionPresets)

    abstract fun getPersistentObjects(): List<GameEntity>
}