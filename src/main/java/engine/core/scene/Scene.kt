package engine.core.scene

import engine.core.session.Session
import engine.core.window.Window
import org.joml.Matrix4f

interface Scene {

    val screenWidth: Float
    val screenHeight: Float

    val renderProjection: Matrix4f

    val context: GameContext

    var isDebugFlag: Boolean

    fun init(
        session: Session,
        intent: SceneIntent? = null,
        isDebugFlag: Boolean = false
    ) {
        this.isDebugFlag = isDebugFlag
        context.addAll(session.getPersistentObjects())
    }

    fun input(window: Window)

    fun update(deltaTime: Float)

    fun render(window: Window)

    fun onSwitch() = Unit

    fun getNextSceneIntent(): SceneIntent
}