package demo.medieval_game.scene

import demo.medieval_game.matrix.MedievalGameMatrixState
import engine.core.scene.Scene
import engine.core.scene.SceneHolder
import engine.core.session.SimpleGamePresets
import engine.feature.matrix.MatrixComputer
import org.joml.Matrix4f

class MedievalGame(
        override val screenWidth: Float,
        override val screenHeight: Float
) : SceneHolder {

    override val sceneMap: MutableMap<String, Scene> = mutableMapOf()
    override var currentScene: Scene? = null

    override val session = MedievalGameSession

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

    init {
        sceneMap["nexus"] = NexusMap(screenWidth, screenHeight, renderProjection)
    }

    override fun init() {
        MatrixComputer.matrixState = MedievalGameMatrixState

        MedievalGameSession.init(
                SimpleGamePresets(
                        screenWidth, screenHeight, renderProjection
                )
        )

        currentScene = StartMap(
                screenWidth,
                screenHeight,
                renderProjection
        ) { switchScene("nexus") }

        currentScene?.init(MedievalGameSession)
    }
}