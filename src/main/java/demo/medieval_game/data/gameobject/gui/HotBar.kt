package demo.medieval_game.data.gameobject.gui

import demo.medieval_game.ShaderController
import demo.medieval_game.data.gameobject.hp.HealthBar
import engine.core.entity.CompositeEntity
import engine.core.render.Model
import engine.core.texture.Texture2D
import org.joml.Matrix4f

class HotBar : CompositeEntity() {

    private var hpBar: HealthBar? = null
    private var manaBar: HealthBar? = null
    private var skills: List<Any> = emptyList()

    private var graphicalComponent: Model? = null

    fun init(renderProjection: Matrix4f) {
        val texturePath = this.javaClass.getResource("/textures/HotBarContainer.png")!!.path

        graphicalComponent = Model(
            texture = Texture2D.createInstance(texturePath)
        ).apply {
            shader = ShaderController.createTexturedShader(renderProjection)
        }
    }
}