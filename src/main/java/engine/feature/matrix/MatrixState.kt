package engine.feature.matrix

import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f

interface MatrixState {

    val nonTranslatedParams: MutableList<SetOfStatic2DParameters>

    fun getResultMatrix(
        posX: Float,
        posY: Float,
        xSize: Float,
        ySize: Float,
        rotationAngle: Float,
        isWorldTranslationEnabled: Boolean = true
    ): Matrix4f
}