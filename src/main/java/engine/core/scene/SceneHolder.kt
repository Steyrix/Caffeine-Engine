package engine.core.scene

import engine.core.session.Session
import engine.core.window.Window

// TODO: fix relation between scene holder and session
interface SceneHolder {

    val session: Session

    var screenWidth: Float
    var screenHeight: Float

    val sceneMap: MutableMap<String, Scene>
    val sharedContext: GameContext

    var currentScene: Scene?

    fun init(width: Float, height: Float)

    fun render(window: Window) {
        currentScene?.render(window)
    }

    fun update(deltaTime: Float) {
        currentScene?.update(deltaTime)
    }

    fun input(window: Window) {
        currentScene?.input(window)
    }

    fun switchScene(nextSceneName: String) {
        currentScene?.onSwitch()
        val intent = currentScene?.getNextSceneIntent()
        currentScene = sceneMap[nextSceneName]
        currentScene?.init(session, intent)
    }

    fun getNextSceneName(intent: SceneIntent): String
}