package engine.core.scene

import engine.core.session.Session
import engine.core.window.Window

interface SceneHolder {

    val session: Session

    val screenWidth: Float
    val screenHeight: Float

    val sceneMap: MutableMap<String, Scene>

    var currentScene: Scene?

    fun init()

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

    fun <I : SceneIntent> getNextSceneName(intent: I): String
}