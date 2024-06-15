package engine.core.game_object

import engine.core.geometry.Point2D

sealed class SpawnOptions {
    abstract val pos: Point2D
}

data class StandartOptions(
    override val pos: Point2D
) : SpawnOptions()