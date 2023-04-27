package engine.core.scene

import engine.core.update.SetOfParameters
import engine.core.window.Window
import org.joml.Matrix4f

interface Scene {

    val screenWidth: Float
    val screenHeight: Float

    val gameContext: MutableList<GameObject>

    fun init(persistentObject: List<GameObject>) {
        gameContext.addAll(persistentObject)
    }

    fun input(window: Window)

    fun update(deltaTime: Float)

    fun render(window: Window)

    fun onSwitch()

    var renderProjection: Matrix4f?
}