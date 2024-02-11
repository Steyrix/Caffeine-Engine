package engine.feature.matrix

import engine.core.update.SetOfStatic2DParameters
import org.joml.Matrix4f
import org.joml.Vector3f

object DefaultMatrixState : MatrixState {

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