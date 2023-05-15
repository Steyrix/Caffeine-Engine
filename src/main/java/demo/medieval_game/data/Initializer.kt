package demo.medieval_game.data

import demo.medieval_game.matrix.MedievalGameMatrixState
import demo.medieval_game.data.gameobject.PlayableCharacter
import demo.medieval_game.data.gameobject.TempSpritesHolder
import engine.core.scene.game_object.GameObject
import engine.core.update.getCenterPoint
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f

object Initializer {

    fun initPersistentObjects(
            screenWidth: Float,
            screenHeight: Float,
            renderProjection: Matrix4f,
            bbCollisionContext: BoundingBoxCollisionContext,
            boxInteractionContext: BoxInteractionContext
    ): List<GameObject> {
        val out = mutableListOf<GameObject>()

        val tempSpritesHolder = TempSpritesHolder().apply {
            init(renderProjection)
        }

        out.add(tempSpritesHolder)

        val character = initPlayableCharacter(
                renderProjection,
                bbCollisionContext,
                tiledCollisionContext = null,
                boxInteractionContext,
                tempSpritesHolder
        )

        out.add(character)

        val centerPoint = characterParameters.getCenterPoint()

        MedievalGameMatrixState.translateWorld(
                screenWidth / 2 - centerPoint.x,
                screenHeight / 2 - centerPoint.y
        )

        return out
    }

    private fun initPlayableCharacter(
            renderProjection: Matrix4f,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            tiledCollisionContext: TiledCollisionContext?,
            boxInteractionContext: BoxInteractionContext,
            tempSpritesHolder: TempSpritesHolder
    ): GameObject {
        return PlayableCharacter().apply {
            init(
                    renderProjection,
                    boundingBoxCollisionContext,
                    tiledCollisionContext,
                    boxInteractionContext,
                    tempSpritesHolder
            )
        }
    }
}