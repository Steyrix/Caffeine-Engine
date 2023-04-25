import engine.core.loop.FixedStepGameLoop
import engine.core.loop.GameLoop
import engine.core.loop.timer.Timer
import engine.core.scene.Scene
import engine.core.window.Window
import java.lang.Exception

class Engine(
    private var scene: Scene
) : Runnable {

    private var window = Window(scene.screenWidth.toInt(), scene.screenHeight.toInt())
    private val timer = Timer()
    private var gameLoop: GameLoop = FixedStepGameLoop(window, timer)

    private fun init() {
        scene.init()
    }

    override fun run() {
        try {
            init()
            gameLoop.loop(
                    inputFunc = {
                        scene.input(window)
                    },
                    renderFunc = {
                        scene.render(window)
                        window.update()
                    },
                    updateFunc = { deltaTime ->
                        scene.update(deltaTime)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}