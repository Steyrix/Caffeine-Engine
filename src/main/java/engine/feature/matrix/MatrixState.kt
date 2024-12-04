package engine.feature.matrix

import engine.core.update.SetOfStaticParameters
import org.joml.Matrix4f
import org.joml.Vector2f

interface MatrixState {

    val worldTranslation: Vector2f
    val tempTranslation: Vector2f

    var worldWidth: Float
    var worldHeight: Float
    var screenWidth: Float
    var screenHeight: Float

    val nonTranslatedParams: MutableList<SetOfStaticParameters>

    fun getResultMatrix(
        posX: Float,
        posY: Float,
        xSize: Float,
        ySize: Float,
        rotationAngle: Float,
        isWorldTranslationEnabled: Boolean = true
    ): Matrix4f
}