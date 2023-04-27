package demo.medieval_game.data

import demo.medieval_game.data.gameobject.Character
import demo.medieval_game.data.gameobject.TempSpritesHolder
import demo.medieval_game.data.starting_level.characterParameters
import engine.core.scene.GameObject
import engine.core.update.getCenterPoint
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.matrix.MatrixState
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

        MatrixState.translate(
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
        return Character().apply {
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