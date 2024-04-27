package engine.feature.matrix

import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

object DefaultMatrixState : MatrixState {

    override val worldTranslation = Vector2f(0f, 0f)
    override val tempTranslation = Vector2f(0f, 0f)

    override var worldWidth = 0f
    override var worldHeight = 0f
    override var screenWidth = 0f
    override var screenHeight = 0f

    override val nonTranslatedParams: MutableList<SetOfStatic2DParameters> = mutableListOf()

    override fun getResultMatrix(
        posX: Float,
        posY: Float,
        xSize: Float,
        ySize: Float,
        rotationAngle: Float,
        isWorldTranslationEnabled: Boolean
    ): Matrix4f {
        val model = Matrix4f().identity()

        // val rotationMatrix = Matrix4f().rotationZ(rotationAngle)
        // val scaleMatrix = Matrix4f().scaleXY(xSize, ySize)

        return model.apply {
            translate(Vector3f(posX, posY, 0f))
            rotateZ(rotationAngle)
            scaleXY(xSize, ySize)
        }
    }
}