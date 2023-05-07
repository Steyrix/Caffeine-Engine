package engine.core.scene

import engine.core.session.Session
import engine.core.window.Window
import org.joml.Matrix4f

interface Scene {

    val screenWidth: Float
    val screenHeight: Float

    var renderProjection: Matrix4f?

    val gameContext: MutableList<GameObject>

    fun init(session: Session) {
        gameContext.addAll(session.getPersistentObjects())
    }

    fun input(window: Window)

    fun update(deltaTime: Float)

    fun render(window: Window)

    fun onSwitch()
}