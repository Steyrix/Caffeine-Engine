package engine.core.scene

import engine.core.window.Window

interface SceneHolder {

    val screenWidth: Float
    val screenHeight: Float

    val sceneMap: MutableMap<String, Scene>

    var currentScene: Scene?

    val persistentGameObjects: MutableList<GameObject>

    fun render(window: Window) {
        currentScene?.render(window)
    }

    fun update(deltaTime: Float) {
        currentScene?.update(deltaTime)
    }

    fun switchScene(nextSceneName: String) {
        currentScene?.onSwitch()
        currentScene = sceneMap[nextSceneName]
    }
}