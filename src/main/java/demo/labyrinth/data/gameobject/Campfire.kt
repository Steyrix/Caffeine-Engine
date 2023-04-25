package demo.labyrinth.data.gameobject

import demo.labyrinth.ShaderController
import demo.labyrinth.data.campfireAnimations
import demo.labyrinth.data.campfireParameters
import engine.core.entity.CompositeEntity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.scene.GameObject
import engine.core.texture.Texture2D
import org.joml.Matrix4f

class Campfire : GameObject {
    override var it: CompositeEntity? = null
    private var graphicalComponent: AnimatedObject2D? = null

    fun init(
            renderProjection: Matrix4f
    ) {
        val frameSizeX = 0.2f
        val frameSizeY = 1.0f

        val texturePath = this.javaClass.getResource("/textures/camp_fire_texture.png")!!.path
        graphicalComponent = AnimatedObject2D(
                frameSizeX = frameSizeX,
                frameSizeY = frameSizeY,
                texture = Texture2D.createInstance(texturePath),
                animations = campfireAnimations
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection)
        }

        it = object : CompositeEntity() {}
        addComponent(graphicalComponent, campfireParameters)
    }

    override fun getZLevel(): Float {
        return campfireParameters.y
    }
}