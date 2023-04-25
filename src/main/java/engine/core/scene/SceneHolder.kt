package engine.core.scene

import engine.core.scene.context.Bundle
import engine.core.window.Window

interface SceneHolder {

    val sceneMap: MutableMap<String, Scene>

    var currentScene: Scene?

    fun render(window: Window) {
        currentScene?.render(window)
    }

    fun update(deltaTime: Float) {
        currentScene?.update(deltaTime)
    }

    fun switchScene(nextSceneName: String) {
        currentScene?.onSwitch()
        val bundle = currentScene?.getBundle()
        currentScene = sceneMap[nextSceneName]
        onSceneChanged(bundle)
    }

    fun onSceneChanged(bundle: Bundle?) {
        currentScene?.init(bundle)
    }
}