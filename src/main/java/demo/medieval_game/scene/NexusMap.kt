package demo.medieval_game.scene

import demo.medieval_game.data.gameobject.Character
import demo.medieval_game.data.gameobject.TempSpritesHolder
import demo.medieval_game.data.starting_level.StartMapInitializer
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.core.window.Window
import engine.feature.tiled.scene.TileMapPreset
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*

class NexusMap(
        preset: TileMapPreset,
        screenWidth: Float,
        screenHeight: Float,
        projection: Matrix4f? = null,
        switchTrigger: () -> Unit = {}
) : MedievalGameScene(preset, screenWidth, screenHeight, projection) {

    override fun init(session: Session, intent: SceneIntent?) {
        super.init(session, intent)

        val tempSpritesHolder = gameContext.find { it is TempSpritesHolder } as? TempSpritesHolder
        val character = gameContext.find { it is Character } as Character

        // TODO: temp
        tiledMap?.let {
            val objects = StartMapInitializer.initAll(
                    renderProjection!!,
                    bbCollisionContext!!,
                    boxInteractionContext!!,
                    tempSpritesHolder!!
            ) { params -> it.createTraverser(params) }

            gameContext.addAll(objects)
            character.updateCollisionContext(tiledCollisionContext)
            character.updateBoundingBox()
        }
    }

    override fun onSwitch(): MedievalGameSceneIntent {
        TODO("Not yet implemented")
    }
}