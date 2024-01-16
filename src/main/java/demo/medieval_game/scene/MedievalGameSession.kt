package demo.medieval_game.scene

import demo.medieval_game.ShaderController
import demo.medieval_game.data.Initializer
import demo.medieval_game.data.gameobject.PlayableCharacter
import engine.core.game_object.GameEntity
import engine.core.session.Session
import engine.core.session.SessionPresets
import engine.core.session.SimpleGamePresets
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.text.TextRenderer
import engine.feature.text.data.TextDataUtils
import java.awt.Dimension

object MedievalGameSession : Session() {

    override val persistentGameEntities: MutableList<GameEntity> = mutableListOf()

    var sessionCharacter: PlayableCharacter? = null
        private set

    // TODO: scene holder should probably be responsible of this
    val bbCollisionContext = BoundingBoxCollisionContext()
    val boxInteractionContext = BoxInteractionContext()

    var textRenderer: TextRenderer? = null

    override fun init(presets: SessionPresets) {
        if (presets !is SimpleGamePresets) return

        initTextRenderer()

        persistentGameEntities.addAll(
            Initializer.initPersistentObjects(
                presets.screenWidth,
                presets.screenHeight,
                bbCollisionContext,
                boxInteractionContext
            )
        )

        sessionCharacter = persistentGameEntities.find { it is PlayableCharacter } as? PlayableCharacter
    }

    override fun getPersistentObjects(): List<GameEntity> {
        return persistentGameEntities.toList()
    }

    private fun initTextRenderer() {
        val fontAtlasPath = this.javaClass.getResource("/textures/SimpleFontAtlas.png")?.path ?: ""

        textRenderer = TextRenderer.getInstance(
            charSizeInAtlas = Dimension(64, 64),
            textureFilePath = fontAtlasPath,
            characters = TextDataUtils.symbolSetSimple,
            initialShader = ShaderController.createTextShader(MedievalGame.renderProjection)
        )
    }
}