package demo.medieval_game.data.gameobject.on_map.chest

import demo.medieval_game.ShaderController
import demo.medieval_game.data.chestAnimations
import demo.medieval_game.data.static_parameters.*
import demo.medieval_game.data.gameobject.gui.bar.HealthBar
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.game_object.SingleGameEntity
import engine.core.loop.GameLoopTimeEvent
import engine.core.loop.SingleTimeEvent
import engine.core.render.AnimatedModel2D
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f

class Chest(
    private val parameters: SetOfStatic2DParameters
) : SingleGameEntity() {

    private var disposalEvent: GameLoopTimeEvent? = null

    fun init(
        renderProjection: Matrix4f,
        boxInteractionContext: BoxInteractionContext,
        path: String
    ) {
        val graphicalComponent = createGraphicalComponent(path, renderProjection)

        val boundingBox = createBoundingBox(renderProjection)
        val controller = ChestController(graphicalComponent)
        val hpBar = createHpBar(renderProjection) {
            controller.isBreaking = true
            disposalEvent?.let { event -> addEvent(event) }
        }

        disposalEvent =
            SingleTimeEvent(
                timeLimit = 1.5f,
                action = { _ ->
                    it?.removeComponent(boundingBox)
                    it?.removeComponent(controller)
                    it?.removeComponent(hpBar)
                    removeEvent(disposalEvent!!)
                },
                initialTime = 0f
            )

        it = object : CompositeEntity() {}

        addComponent(graphicalComponent, parameters)
        addComponent(controller, parameters)
        addComponent(boundingBox, parameters)
        addComponent(hpBar, parameters)

        boxInteractionContext.addAgent(it as Entity, boundingBox)
    }

    private fun createGraphicalComponent(
        path: String,
        renderProjection: Matrix4f
    ): AnimatedModel2D {
        // TODO: keep in presets
        val frameWidth = 0.166f
        val frameHeight = 0.5f

        val texturePath = this.javaClass.getResource(path)!!.path
        return AnimatedModel2D(
            frameWidth = frameWidth,
            frameHeight = frameHeight,
            texture = Texture2D.createInstance(texturePath),
            animations = chestAnimations.map { it.copy() }
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection)
            stencilShader = ShaderController.createSingleColorShader(renderProjection)
        }
    }

    private fun createBoundingBox(renderProjection: Matrix4f) =
        BoundingBox(
            xSize = CHEST_BOX_WIDTH,
            ySize = CHEST_BOX_HEIGHT,
            isSizeBoundToHolder = false,
            xOffset = CHEST_BOX_X_OFFSET,
            yOffset = CHEST_BOX_Y_OFFSET
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection)
        }

    private fun createHpBar(
        renderProjection: Matrix4f,
        onEmpty: () -> Unit
    ): HealthBar {
        return HealthBar(
            parameters,
            createDefaultHpBarParams(),
            renderProjection,
            texturePath = this.javaClass.getResource("/textures/gui/HealthBarAtlas.png")!!.path,
            onFilledChange = { hpAmount ->
                if (hpAmount <= 0) {
                    onEmpty.invoke()
                }
            }
        )
    }
}