package demo.medieval_game.data

import demo.medieval_game.matrix.MedievalGameMatrixState
import demo.medieval_game.data.gameobject.character.PlayableCharacter
import demo.medieval_game.data.static_parameters.characterParameters
import engine.core.game_object.GameEntity
import engine.core.geometry.Point2D
import engine.core.update.getCenterPoint

object Initializer {

    fun initPersistentObjects(
        screenWidth: Float,
        screenHeight: Float
    ): List<GameEntity> {
        val out = mutableListOf<GameEntity>()

        val character = initPlayableCharacter()

        out.add(character)

        val centerPoint = character.getParams().getCenterPoint()

        MedievalGameMatrixState.translateWorld(
            screenWidth / 2 - centerPoint.x,
            screenHeight / 2 - centerPoint.y
        )

        return out
    }

    private fun initPlayableCharacter(): PlayableCharacter {
        return PlayableCharacter(characterParameters).apply {
            init()
            spawn(Point2D(500f, 500f))
        }
    }
}