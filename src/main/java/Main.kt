import demo.labyrinth.LabyrinthDemo
import java.lang.Exception
import kotlin.system.exitProcess

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val mockObj = LabyrinthDemo(999.375f, 999.375f)

            val engine = Engine(mockObj)
            engine.run()
        } catch (e: Exception) {
            e.printStackTrace()
            exitProcess(-1)
        }
    }
}