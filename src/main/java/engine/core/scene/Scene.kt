package engine.core.scene

import engine.core.window.Window
import org.joml.Matrix4f

interface Scene {

    val screenWidth: Float
    val screenHeight: Float
    fun init()

    fun input(window: Window)

    fun update(deltaTime: Float)

    fun render(window: Window)

    var renderProjection: Matrix4f?
}