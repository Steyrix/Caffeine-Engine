package demo.medieval_game.data.gameobject.on_map

import demo.medieval_game.ShaderController
import demo.medieval_game.data.campfireAnimations
import engine.core.entity.CompositeEntity
import engine.core.render.AnimatedModel2D
import engine.core.game_object.SingleGameEntity
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class Campfire(
    private val parameters: SetOfStatic2DParameters
) : SingleGameEntity() {

    fun init(
        renderProjection: Matrix4f
    ) {
        val frameWidth = 0.1428f
        val frameHeight = 1.0f

        val texturePath = this.javaClass.getResource("/textures/LowBonfireAtlasWithContour.png")!!.path
        val graphicalComponent = AnimatedModel2D(
            frameWidth = frameWidth,
            frameHeight = frameHeight,
            texture = Texture2D.createInstance(texturePath),
            animations = campfireAnimations
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection)
        }

        it = object : CompositeEntity() {}
        addComponent(graphicalComponent, parameters)
    }

    override fun getZLevel(): Float {
        return parameters.y + parameters.ySize + 1f
    }
}