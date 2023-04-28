package engine.core.scene

import engine.core.window.Window

interface SceneHolder {

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
        currentScene = sceneMap[nextSceneName]
    }
}