package demo.medieval_game.scene

import demo.medieval_game.data.Initializer
import demo.medieval_game.data.gameobject.Character
import demo.medieval_game.data.gameobject.TempSpritesHolder
import engine.core.scene.GameObject
import engine.core.session.Session
import engine.core.session.SessionPresets
import engine.core.session.SimpleGamePresets
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext

object MedievalGameSession : Session() {

    override val persistentGameObjects: MutableList<GameObject> = mutableListOf()

    var playableCharacter: Character? = null
        private set

    var tempSpritesHolder: TempSpritesHolder? = null
        private set

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

        playableCharacter = persistentGameObjects.find { it is Character } as? Character
        tempSpritesHolder = persistentGameObjects.find { it is TempSpritesHolder } as? TempSpritesHolder
    }

    override fun getPersistentObjects(): List<GameObject> {
        return persistentGameObjects.toList()
    }
}