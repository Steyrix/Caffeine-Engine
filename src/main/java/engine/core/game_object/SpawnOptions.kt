package engine.core.game_object

import java.awt.geom.Point2D

sealed class SpawnOptions {
    abstract val pos: Point2D
}

data class StandartOptions(
    override val pos: Point2D
) : SpawnOptions()