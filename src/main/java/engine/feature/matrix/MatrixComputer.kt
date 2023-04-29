package engine.feature.matrix

import org.joml.Matrix4f

object MatrixComputer {

    var matrixState: MatrixState = DefaultMatrixState

    fun getResultMatrix(
            posX: Float,
            posY: Float,
            xSize: Float,
            ySize: Float,
            rotationAngle: Float
    ): Matrix4f = matrixState.getResultMatrix(posX, posY, xSize, ySize, rotationAngle)
}