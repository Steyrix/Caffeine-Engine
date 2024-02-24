import demo.medieval_game.scene.MedievalGame
import java.lang.Exception
import kotlin.system.exitProcess

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            // TODO: pass from windows
            val mockObj = MedievalGame(1728f, 1117f)
            val engine = Engine(mockObj)
            engine.run()
        } catch (e: Exception) {
            e.printStackTrace()
            exitProcess(-1)
        }
    }
}