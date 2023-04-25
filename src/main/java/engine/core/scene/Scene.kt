package engine.core.scene

import engine.core.scene.context.Bundle
import engine.core.window.Window
import org.joml.Matrix4f

interface Scene {

    val screenWidth: Float
    val screenHeight: Float
    fun init(bundle: Bundle? = null)

    fun input(window: Window)

    fun update(deltaTime: Float)

    fun render(window: Window)

    fun onSwitch()

    fun getBundle(): Bundle?

    var renderProjection: Matrix4f?
}