package demo.medieval_game.data.gameobject.gui.inventory

import demo.medieval_game.ShaderController
import engine.core.controllable.Controllable
import engine.core.entity.CompositeEntity
import engine.core.geometry.Point2D
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class InventoryGuiContainer(
    val parameters: SetOfStatic2DParameters
) : CompositeEntity(), Controllable {

    private var graphicalComponent: Model? = null

    fun init(renderProjection: Matrix4f) {
        val texturePath = this.javaClass.getResource("/textures/gui/InventoryGuiContainer.png")!!.path

        graphicalComponent = Model(
            texture = Texture2D.createInstance(texturePath)
        ).apply {
            shader = ShaderController.createTexturedShader(renderProjection)
            zLevel = 1f
            isPartOfWorldTranslation = false
        }

        addComponent(graphicalComponent!!, parameters)
    }

    fun updatePosition(pos: Point2D) {
        parameters.x = pos.x
        parameters.y = pos.y
    }
}