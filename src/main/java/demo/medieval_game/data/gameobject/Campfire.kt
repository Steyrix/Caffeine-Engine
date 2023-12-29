package demo.medieval_game.data.gameobject

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

    private var graphicalComponent: AnimatedModel2D? = null

    fun init(
            renderProjection: Matrix4f
    ) {
        val frameSizeX = 0.2f
        val frameSizeY = 1.0f

        val texturePath = this.javaClass.getResource("/textures/camp_fire_texture.png")!!.path
        graphicalComponent = AnimatedModel2D(
                frameSizeX = frameSizeX,
                frameSizeY = frameSizeY,
                texture = Texture2D.createInstance(texturePath),
                animations = campfireAnimations
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection)
        }

        it = object : CompositeEntity() {}
        addComponent(graphicalComponent, parameters)
    }

    override fun getZLevel(): Float {
        return parameters.y
    }
}