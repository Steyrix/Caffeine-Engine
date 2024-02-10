package demo.medieval_game.data.gameobject.gui.bar

import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

class ManaBar(
    objParams: SetOfParameters,
    barParams: SetOfStatic2DParameters,
    projection: Matrix4f,
    isBoundToParams: Boolean,
    onFilledChange: (Float) -> Unit = {},
    texturePath: String
) : ResourceBar(
    objParams,
    barParams,
    projection,
    onFilledChange,
    isBoundToParams,
    texturePath
)