package demo.medieval_game.scene

import demo.medieval_game.data.Initializer
import engine.core.scene.GameObject
import engine.core.scene.Scene
import engine.core.scene.SceneHolder
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f

class MedievalGame(
        override val screenWidth: Float,
        override val screenHeight: Float
) : SceneHolder {

    override val sceneMap: MutableMap<String, Scene> = mutableMapOf()
    override var currentScene: Scene? = null
    override val persistentGameObjects: MutableList<GameObject> = mutableListOf()

    private val bbCollisionContext = BoundingBoxCollisionContext()
    private val boxInteractionContext = BoxInteractionContext()

    private val renderProjection: Matrix4f =
            Matrix4f()
                    .ortho(
                            0f,
                            screenWidth,
                            screenHeight,
                            0f,
                            0f,
                            1f
                    )

    override fun init() {
        persistentGameObjects.addAll(
                Initializer.initPersistentObjects(
                        screenWidth,
                        screenHeight,
                        renderProjection,
                        bbCollisionContext,
                        boxInteractionContext
                )
        )

        currentScene = StartingMapDemo(
                screenWidth,
                screenHeight,
                renderProjection,
                boxInteractionContext,
                bbCollisionContext
        )

        currentScene?.init(persistentGameObjects)
    }
}