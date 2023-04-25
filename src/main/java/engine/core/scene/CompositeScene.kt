package engine.core.scene

import engine.core.window.Window

interface CompositeScene : Scene {

    val sceneMap: MutableMap<String, Scene>

    var currentScene: Scene?

    override fun init() {
        currentScene?.init()
    }

    override fun render(window: Window) {
        currentScene?.render(window)
    }

    override fun update(deltaTime: Float) {
        currentScene?.update(deltaTime)
    }

    fun switchScene(nextSceneName: String) {
        currentScene = sceneMap[nextSceneName]
    }
}