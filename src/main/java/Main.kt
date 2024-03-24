import engine.core.game_object.GameEntity
import engine.core.scene.GameContext
import engine.core.scene.Scene
import engine.core.scene.SceneHolder
import engine.core.scene.SceneIntent
import engine.core.session.Session
import engine.core.session.SessionPresets
import engine.feature.interaction.broadcast.EventReceiver
import java.lang.Exception
import kotlin.system.exitProcess

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            // STUB
            val mockObj = object : SceneHolder {
                override val session: Session = object : Session() {
                    override val persistentGameEntities: MutableList<GameEntity> = mutableListOf()
                    override fun init(presets: SessionPresets) {}
                    override fun getPersistentObjects(): List<GameEntity> {
                        return emptyList()
                    }
                }
                override var screenWidth: Float = 1000f
                override var screenHeight: Float = 1000f
                override val sceneMap: MutableMap<String, Scene> = mutableMapOf()
                override val sharedContext: GameContext = GameContext()
                override val sharedReceivers: MutableList<EventReceiver> = mutableListOf()
                override var currentScene: Scene? = null

                override fun init(width: Float, height: Float) {}

                override fun getNextSceneName(intent: SceneIntent): String {
                    return "empty"
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