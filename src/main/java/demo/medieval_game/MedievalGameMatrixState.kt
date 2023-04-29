package demo.medieval_game

import engine.feature.matrix.MatrixState
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.abs

object MedievalGameMatrixState : MatrixState {

    val worldTranslation = Vector2f(0f, 0f)

    override fun getResultMatrix(
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

    fun translateWorld(x: Float, y: Float) {
        if (isTranslationPossible(x)) {
            worldTranslation.x += x
        }
        worldTranslation.y += y
    }

    fun isTranslationPossible(x: Float, worldWidth: Float = 1500f, screenWidth: Float = 999.375f): Boolean {
        return (screenWidth + abs(worldTranslation.x + x) < worldWidth)
    }
}