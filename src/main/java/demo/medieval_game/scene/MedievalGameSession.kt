package demo.medieval_game.scene

import demo.medieval_game.data.Initializer
import demo.medieval_game.data.gameobject.Character
import engine.core.scene.GameObject
import engine.core.session.Session
import engine.core.session.SessionPresets
import engine.core.session.SimpleGamePresets
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext

object MedievalGameSession : Session() {

    override val persistentGameObjects: MutableList<GameObject> = mutableListOf()

    private var character: Character? = null

    val bbCollisionContext = BoundingBoxCollisionContext()
    val boxInteractionContext = BoxInteractionContext()

    override fun init(presets: SessionPresets) {
        if (presets !is SimpleGamePresets) return

        persistentGameObjects.addAll(
                Initializer.initPersistentObjects(
                        presets.screenWidth,
                        presets.screenHeight,
                        presets.renderProjection,
                        bbCollisionContext,
                        boxInteractionContext
                )
        )

        character = persistentGameObjects.find { it is Character } as? Character
    }

    override fun getPersistentObjects(): List<GameObject> {
        return persistentGameObjects.toList()
    }

    fun getCharacter(): Character? {
        return character
    }
}