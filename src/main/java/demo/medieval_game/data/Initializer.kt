package demo.medieval_game.data

import demo.medieval_game.matrix.MedievalGameMatrixState
import demo.medieval_game.data.gameobject.PlayableCharacter
import demo.medieval_game.data.static_parameters.characterParameters
import engine.core.game_object.GameEntity
import engine.core.geometry.Point2D
import engine.core.update.getCenterPoint
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext

object Initializer {

    fun initPersistentObjects(
        screenWidth: Float,
        screenHeight: Float,
        bbCollisionContext: BoundingBoxCollisionContext,
        boxInteractionContext: BoxInteractionContext
    ): List<GameEntity> {
        val out = mutableListOf<GameEntity>()

        val character = initPlayableCharacter(bbCollisionContext, boxInteractionContext)

        out.add(character)

        val centerPoint = character.getParams().getCenterPoint()

        MedievalGameMatrixState.translateWorld(
            screenWidth / 2 - centerPoint.x,
            screenHeight / 2 - centerPoint.y
        )

        return out
    }

    private fun initPlayableCharacter(
        boundingBoxCollisionContext: BoundingBoxCollisionContext,
        boxInteractionContext: BoxInteractionContext
    ): PlayableCharacter {
        return PlayableCharacter(
            characterParameters
        ).apply {
            init(boundingBoxCollisionContext, boxInteractionContext)
            spawn(Point2D(500f, 500f))
        }
    }
}