package demo.medieval_game.scene

import demo.medieval_game.data.characterParameters
import demo.medieval_game.data.gameobject.*
import demo.medieval_game.data.starting_level.StartMapInitializer
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.feature.geometry.Point2D
import engine.feature.tiled.scene.TileMapPreset
import org.joml.Matrix4f

class StartMap(
        preset: TileMapPreset,
        screenWidth: Float,
        screenHeight: Float,
        projection: Matrix4f? = null,
        private val switchTrigger: () -> Unit = {}
) : MedievalGameScene(preset, screenWidth, screenHeight, projection) {

    private var isFading = false

    override fun init(session: Session, intent: SceneIntent?) {
        super.init(session, intent)

        tempSpritesHolder = gameContext.find { it is TempSpritesHolder } as? TempSpritesHolder
        character = gameContext.find { it is Character } as Character

        tiledMap?.let {
            val objects = StartMapInitializer.initAll(
                    renderProjection!!,
                    bbCollisionContext!!,
                    boxInteractionContext!!,
                    tempSpritesHolder!!
            ) { params -> it.createTraverser(params) }

            gameContext.addAll(objects)
            character?.updateCollisionContext(tiledCollisionContext)
            character?.updateBoundingBox()
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (character?.isOutOfMap() == true && !isFading) {
            isFading = true
            tempSpritesHolder?.startScreenFading(
                    tiledMap?.worldSize?.x ?: 0f,
                    tiledMap?.worldSize?.y ?: 0f,
            ) {
                switchTrigger.invoke()
            }
        }
    }

    override fun getNextSceneIntent(): MedievalGameSceneIntent {
        return MedievalGameSceneIntent(
                direction = character!!.getDirection(),
                previousScenePos = Point2D(characterParameters.x, characterParameters.y)
        )
    }

    override fun handleMapTransaction(intent: MedievalGameSceneIntent) = Unit

    override fun postMapTransactionAction() = Unit
}