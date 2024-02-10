package demo.medieval_game.data.gameobject.gui

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters

class HotBarSmallCell(
    private val parameters: SetOfStatic2DParameters,
    private val containerParameters: SetOfStatic2DParameters,
    private val content: Pair<Entity, SetOfParameters>,
    texturePath: String
) : CompositeEntity() {

    private val graphicalComponent: Model

    init {
        graphicalComponent = Model(
            texture = Texture2D.createInstance(texturePath)
        )
        addComponent(graphicalComponent, parameters)
        addComponent(content.first, content.second)
    }

}