package demo.medieval_game.data.gameobject.on_map

import demo.medieval_game.ShaderController
import demo.medieval_game.data.chestAnimations
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
        val frameWidth = 0.166f
        val frameHeight = 0.5f

        val texturePath = this.javaClass.getResource(path)!!.path
        val graphicalComponent = AnimatedModel2D(
            frameWidth = frameWidth,
            frameHeight = frameHeight,
            texture = Texture2D.createInstance(texturePath),
            animations = chestAnimations.map { it.copy() }
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection)
        }

        val boundingBox = BoundingBox(
            xSize = parameters.xSize - 50f,
            ySize = parameters.ySize - 50f,
            isSizeBoundToHolder = false,
            xOffset = 25f,
            yOffset = 25f
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection)
        }

        it = object : CompositeEntity() {}

        addComponent(graphicalComponent, parameters)
        addComponent(ChestController(graphicalComponent), parameters)
        addComponent(boundingBox, parameters)

        boxInteractionContext.addAgent(it as Entity, boundingBox)
    }
}