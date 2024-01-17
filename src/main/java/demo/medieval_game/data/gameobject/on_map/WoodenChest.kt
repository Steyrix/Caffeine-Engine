package demo.medieval_game.data.gameobject.on_map

import demo.medieval_game.ShaderController
import demo.medieval_game.data.chestAnimations
import engine.core.entity.CompositeEntity
import engine.core.game_object.SingleGameEntity
import engine.core.render.AnimatedModel2D
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class WoodenChest(
    private val parameters: SetOfStatic2DParameters
) : SingleGameEntity() {

    fun init(
        renderProjection: Matrix4f
    ) {
        val frameWidth = 0.166f
        val frameHeight = 0.5f

        val texturePath = this.javaClass.getResource("/textures/WoodenChestAtlas.png")!!.path
        val graphicalComponent = AnimatedModel2D(
            frameWidth = frameWidth,
            frameHeight = frameHeight,
            texture = Texture2D.createInstance(texturePath),
            animations = chestAnimations
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection)
        }

        it = object : CompositeEntity() {}

        addComponent(graphicalComponent, parameters)
    }
}