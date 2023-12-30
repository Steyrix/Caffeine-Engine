package demo.medieval_game.scene

import demo.medieval_game.data.Initializer
import demo.medieval_game.data.gameobject.PlayableCharacter
import demo.medieval_game.data.gameobject.SharedSpritesHolder
import demo.medieval_game.data.gameobject.TempSpritesHolder
import engine.core.game_object.GameEntity
import engine.core.session.Session
import engine.core.session.SessionPresets
import engine.core.session.SimpleGamePresets
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext

object MedievalGameSession : Session() {

    override val persistentGameEntities: MutableList<GameEntity> = mutableListOf()

    var sessionCharacter: PlayableCharacter? = null
        private set

    var tempSpritesHolder: TempSpritesHolder? = null
        private set

    var sharedSpritesHolder: SharedSpritesHolder? = null
        private set

    val bbCollisionContext = BoundingBoxCollisionContext()
    val boxInteractionContext = BoxInteractionContext()

    override fun init(presets: SessionPresets) {
        if (presets !is SimpleGamePresets) return

        persistentGameEntities.addAll(
                Initializer.initPersistentObjects(
                        presets.screenWidth,
                        presets.screenHeight,
                        presets.renderProjection,
                        bbCollisionContext,
                        boxInteractionContext
                )
        )

        sessionCharacter = persistentGameEntities.find { it is PlayableCharacter } as? PlayableCharacter
        tempSpritesHolder = persistentGameEntities.find { it is TempSpritesHolder } as? TempSpritesHolder
        sharedSpritesHolder = SharedSpritesHolder().apply { init(presets.renderProjection) }
    }

    override fun getPersistentObjects(): List<GameEntity> {
        return persistentGameEntities.toList()
    }
}