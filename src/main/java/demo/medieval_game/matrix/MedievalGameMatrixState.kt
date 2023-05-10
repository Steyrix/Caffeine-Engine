package demo.medieval_game.matrix

import demo.medieval_game.data.characterParameters
import engine.core.controllable.Direction
import engine.core.update.getCenterPoint
import engine.feature.matrix.MatrixState
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.abs

object MedievalGameMatrixState : MatrixState {

    val worldTranslation = Vector2f(0f, 0f)
    val tempTranslation = Vector2f(0f, 0f)

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
        if (isHorizontalTranslationPossible(x)) {
            if (tempTranslation.x > 0f) {
                tempTranslation.x -= abs(x)
            } else {
                tempTranslation.x = 0f
                worldTranslation.x += x
            }
        } else {
            tempTranslation.x += abs(x)
        }

        if (isVerticalTranslationPossible(y)) {
            if (tempTranslation.y > 0f) {
                tempTranslation.y -= abs(y)
            } else {
                tempTranslation.y = 0f
                worldTranslation.y += y
            }
        } else {
            tempTranslation.y += abs(y)
        }
    }

    private fun isHorizontalTranslationPossible(x: Float, worldWidth: Float = 1500f, screenWidth: Float = 999.375f): Boolean {
        return (screenWidth + abs(worldTranslation.x + x) < worldWidth)
                && (worldTranslation.x + x) < 0
    }

    private fun isVerticalTranslationPossible(y: Float, worldHeight: Float = 1500f, screenHeight: Float = 999.375f): Boolean {
        return (screenHeight + abs(worldTranslation.y + y) < worldHeight)
                && (worldTranslation.y + y) < 0
    }

    fun handleMapTransaction(
            direction: Direction,
            screenWidth: Float,
            screenHeight: Float,
            worldWidth: Float,
            worldHeight: Float
    ) {
        var xMod = 0f
        var yMod = 0f

        when(direction) {
            Direction.RIGHT -> {
                characterParameters.x = 0f
                xMod = 1f
            }
            Direction.LEFT -> {
                characterParameters.x = worldWidth - characterParameters.xSize
                xMod = 1f
            }
            Direction.UP -> {
                characterParameters.y = worldHeight - characterParameters.ySize
                yMod = 1f
            }
            Direction.DOWN -> {
                characterParameters.y = 0f
                yMod = 1f
            }
        }

        worldTranslation.x *= yMod
        worldTranslation.y *= xMod
        tempTranslation.x *= yMod
        tempTranslation.y *= xMod

        val centerPoint = characterParameters.getCenterPoint()
        val horizontalTranslation = screenWidth - centerPoint.x
        val verticalTranslation = screenHeight - centerPoint.y

        translateWorld(
                horizontalTranslation * xMod,
                verticalTranslation * yMod
        )
    }

    fun postMapTransactionAction(
            isHorizontalTransaction: Boolean,
            screenWidth: Float,
            screenHeight: Float
    ) {
        if (isHorizontalTransaction) {
            tempTranslation.x = screenWidth / 2 - characterParameters.xSize / 2
        } else {
            tempTranslation.y = screenHeight / 2 - characterParameters.ySize / 2
        }
    }
}