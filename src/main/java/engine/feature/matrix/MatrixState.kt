package engine.feature.matrix

import org.joml.Matrix4f

interface MatrixState {
    fun getResultMatrix(
        posX: Float,
        posY: Float,
        xSize: Float,
        ySize: Float,
        rotationAngle: Float
    ): Matrix4f
}