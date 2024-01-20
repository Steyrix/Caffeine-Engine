package demo.medieval_game.data.gameobject.on_map.chest

import demo.medieval_game.ShaderController
import demo.medieval_game.data.chestAnimations
import demo.medieval_game.hp.HealthBar
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.game_object.SingleGameEntity
import engine.core.render.AnimatedModel2D
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f

class Chest(
    private val parameters: SetOfStatic2DParameters
) : SingleGameEntity() {

    fun init(
        renderProjection: Matrix4f,
        boxInteractionContext: BoxInteractionContext,
        path: String
    ) {
        val graphicalComponent = createGraphicalComponent(path, renderProjection)

        val boundingBox = createBoundingBox(renderProjection)
        val controller = ChestController(graphicalComponent)
        val hpBar = createHpBar(renderProjection, controller)

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
        }
    }

    private fun createBoundingBox(renderProjection: Matrix4f) =
        BoundingBox(
            xSize = parameters.xSize - 50f,
            ySize = parameters.ySize - 50f,
            isSizeBoundToHolder = false,
            xOffset = 25f,
            yOffset = 25f
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection)
        }

    private fun createHpBar(
        renderProjection: Matrix4f,
        controller: ChestController
    ): HealthBar {
        // TODO: keep in presets
        val defaultBarParams = SetOfStatic2DParameters(
            x = 0f,
            y = 0f,
            xSize = 50f,
            ySize = 12.5f,
            rotationAngle = 0f
        )

        return HealthBar(
            parameters,
            defaultBarParams,
            renderProjection
        ).apply {
            onEmptyCallback = {
                controller.isBreaking = true
            }
        }
    }
}