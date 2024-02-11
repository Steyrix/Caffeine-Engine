package demo.medieval_game.data.gameobject.gui

import demo.medieval_game.ShaderController
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class HotBarCellSmall(
    val renderProjection: Matrix4f,
    val parameters: SetOfStatic2DParameters,
    private val containerParameters: SetOfStatic2DParameters,
    private val content: Pair<Entity, SetOfParameters>? = null,
    texturePath: String
) : CompositeEntity() {

    private val graphicalComponent: Model

    init {
        graphicalComponent = Model(
            texture = Texture2D.createInstance(texturePath)
        ).apply {
            shader = ShaderController.createTexturedShader(renderProjection)
            isPartOfWorldTranslation = false
        }
        addComponent(graphicalComponent, parameters)

        content?.let {
            addComponent(content.first, content.second)
        }
    }

}