package engine.feature.matrix

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

object MatrixComputer {

    var worldTranslation = Vector2f(0f, 0f)

    fun getResultMatrix(
            posX: Float,
            posY: Float,
            xSize: Float,
            ySize: Float,
            rotationAngle: Float
    ): Matrix4f {
        val model = Matrix4f().identity()

        // val rotationMatrix = Matrix4f().rotationZ(rotationAngle)
        // val scaleMatrix = Matrix4f().scaleXY(xSize, ySize)

        return model.apply {
            translate(Vector3f(posX + worldTranslation.x, posY + worldTranslation.y, 0f))
            rotateZ(rotationAngle)
            scaleXY(xSize, ySize)
        }
    }
}