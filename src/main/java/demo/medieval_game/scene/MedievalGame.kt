package demo.medieval_game.scene

import demo.medieval_game.data.gameobject.SharedSpritesHolder
import demo.medieval_game.data.starting_level.getNexusMapPreset
import demo.medieval_game.data.starting_level.getStartingMapPreset
import demo.medieval_game.matrix.MedievalGameMatrixState
import engine.core.scene.Scene
import engine.core.scene.SceneHolder
import engine.core.scene.SceneIntent
import engine.core.session.SimpleGamePresets
import engine.core.window.Window
import engine.feature.matrix.MatrixComputer
import org.joml.Matrix4f

class MedievalGame(
        override val screenWidth: Float,
        override val screenHeight: Float
) : SceneHolder {

    // TODO: initialize
    private val maps: MutableMap<Int, MutableList<Scene>> = mutableMapOf()

    override val sceneMap: MutableMap<String, Scene> = mutableMapOf()
    override var currentScene: Scene? = null

    override val session = MedievalGameSession

    private var sharedSprites: SharedSpritesHolder? = null

    private val renderProjection: Matrix4f =
            Matrix4f()
                    .ortho(
                            0f,
                            screenWidth,
                            screenHeight,
                            0f,
                            0f,
                            1f
                    )

    // TODO: deserialize maps from presets
    init {
        sceneMap["nexus"] = NexusMap(getNexusMapPreset(), screenWidth, screenHeight, renderProjection)
    }

    override fun init() {
        MatrixComputer.matrixState = MedievalGameMatrixState

        MedievalGameSession.init(
                SimpleGamePresets(
                        screenWidth, screenHeight, renderProjection
                )
        )

        sharedSprites = MedievalGameSession.sharedSpritesHolder

        currentScene = StartMap(
                getStartingMapPreset(screenWidth, screenHeight),
                screenWidth,
                screenHeight,
                renderProjection
        ) {
            switchScene("nexus")
        }

        currentScene?.init(MedievalGameSession)
    }

    override fun switchScene(nextSceneName: String) {
        renderTransition {
            super.switchScene(nextSceneName)
        }
    }

    override fun getNextSceneName(intent: SceneIntent): String {
        (intent as? MedievalGameSceneIntent)?.let {
            return ""
        } ?: throw IllegalStateException()
    }

    private fun renderTransition(
            onFinish: () -> Unit
    ) {
        sharedSprites?.startScreenFading(
                screenWidth * 2,
                screenHeight * 2,
                onFinish
        )
    }

    override fun render(window: Window) {
        super.render(window)
        sharedSprites?.draw()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        sharedSprites?.update(deltaTime)
    }
}