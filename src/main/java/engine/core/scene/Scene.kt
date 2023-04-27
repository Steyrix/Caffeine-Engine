package engine.core.scene

import engine.core.update.SetOfParameters
import engine.core.window.Window
import org.joml.Matrix4f

interface Scene {

    val screenWidth: Float
    val screenHeight: Float

    val gameContext: MutableMap<GameObject, SetOfParameters>
    fun init()

    fun input(window: Window)

    fun update(deltaTime: Float)

    fun render(window: Window)

    fun onSwitch()

    var renderProjection: Matrix4f?
}