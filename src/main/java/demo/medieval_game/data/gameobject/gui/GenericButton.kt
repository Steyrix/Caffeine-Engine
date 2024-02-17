package demo.medieval_game.data.gameobject.gui

import engine.core.entity.CompositeEntity

class GenericButton(
    onClick: (Any?) -> Unit,
    texturePath: String
) : CompositeEntity() {

    private var isPressed = false

}