import engine.core.scene.Scene
import engine.core.window.Window
import java.lang.Exception
import kotlin.system.exitProcess

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val mockObj = object : Scene {
                override fun init() {
                    TODO("Not yet implemented")
                }

                override fun input(window: Window) {
                    TODO("Not yet implemented")
                }

                override fun update(deltaTime: Float) {
                    TODO("Not yet implemented")
                }

                override fun render(window: Window) {
                    TODO("Not yet implemented")
                }
            }

            val engine = Engine(mockObj)
            engine.run()
        } catch (e: Exception) {
            e.printStackTrace()
            exitProcess(-1)
        }
    }
}