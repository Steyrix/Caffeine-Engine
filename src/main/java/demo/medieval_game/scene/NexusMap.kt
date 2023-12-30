package demo.medieval_game.scene

import demo.medieval_game.data.characterParameters
import demo.medieval_game.data.starting_level.StartMapInitializer
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.feature.tiled.scene.TileMapPreset
import org.joml.Matrix4f

class NexusMap(
        preset: TileMapPreset,
        screenWidth: Float,
        screenHeight: Float,
        projection: Matrix4f,
        switchTrigger: () -> Unit = {}
) : MedievalGameScene(preset, screenWidth, screenHeight, projection) {

    override fun init(session: Session, intent: SceneIntent?) {
        super.init(session, intent)

        // TODO: temp
        tiledMap?.let {
            val objects = StartMapInitializer.initAll(
                    bbCollisionContext!!,
                    boxInteractionContext!!,
                    tempSpritesHolder!!
            ) { params -> it.createTraverser(params, characterParameters) }
            context.addAll(objects)
            character?.updateCollisionContext(tiledCollisionContext)
            character?.updateBoundingBox()
        }
    }

    override fun getNextSceneIntent(): MedievalGameSceneIntent {
        TODO("Not yet implemented")
    }
}