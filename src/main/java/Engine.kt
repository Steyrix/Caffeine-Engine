import engine.core.loop.FixedStepGameLoop
import engine.core.loop.GameLoop
import engine.core.loop.timer.Timer
import engine.core.scene.SceneHolder
import engine.core.window.Window
import java.lang.Exception

class Engine(
    private var sceneHolder: SceneHolder
) : Runnable {

    private var window = Window(sceneHolder.screenWidth.toInt(), sceneHolder.screenHeight.toInt())
    private val timer = Timer()
    private var gameLoop: GameLoop = FixedStepGameLoop(window, timer)

    private fun init() {
        sceneHolder.init(window.width.toFloat(), window.height.toFloat())
    }

    override fun run() {
        try {
            init()
            gameLoop.loop(
                inputFunc = {
                    sceneHolder.input(window)
                },
                renderFunc = {
                    sceneHolder.render(window)
                    window.update()
                },
                updateFunc = { deltaTime ->
                    sceneHolder.update(deltaTime)
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}