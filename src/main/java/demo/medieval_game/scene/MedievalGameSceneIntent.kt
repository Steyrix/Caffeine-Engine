package demo.medieval_game.scene

import engine.core.controllable.Direction
import engine.core.scene.SceneIntent
import engine.core.geometry.Point2D

data class MedievalGameSceneIntent(
        val direction: Direction,
        val previousScenePos: Point2D
): SceneIntent
