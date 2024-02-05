package demo.medieval_game.scene

import demo.medieval_game.data.static_parameters.characterParameters
import demo.medieval_game.data.gameobject.*
import demo.medieval_game.data.starting_level.StartMapInitializer
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.core.window.Window
import engine.core.geometry.Point2D
import engine.feature.tiled.scene.TileMapPreset
import org.joml.Matrix4f
import java.awt.Dimension

class StartMap(
    preset: TileMapPreset,
    screenWidth: Float,
    screenHeight: Float,
    projection: Matrix4f,
    private val switchTrigger: () -> Unit = {}
) : MedievalGameScene(preset, screenWidth, screenHeight, projection) {

    private var isFading = false

    override fun init(session: Session, intent: SceneIntent?) {
        super.init(session, intent)

        textRenderer = (session as MedievalGameSession).textRenderer

        character = context.find { it is PlayableCharacter } as PlayableCharacter

        tiledMap?.let {
            val objects = StartMapInitializer.initAll(
                bbCollisionContext!!,
                boxInteractionContext!!,
            ) { params -> it.createTraverser(params, characterParameters) }

            context.addAll(objects)

            tiledCollisionContext?.let {
                character?.addTiledCollider(it)
            }
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (character?.isOutOfMap() == true && !isFading) {
            isFading = true
            switchTrigger.invoke()
        }
    }

    override fun render(window: Window) {
        super.render(window)
        textRenderer?.drawText(
            "HELLO WORLD",
            Dimension(32, 64),
            pos = Point2D(0f, 100f)
        )
    }

    override fun input(window: Window) {
        if (isFading) return
        super.input(window)
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