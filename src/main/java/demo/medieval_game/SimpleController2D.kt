package demo.medieval_game

import demo.medieval_game.data.gameobject.npc.HumanoidAnimationController
import demo.medieval_game.data.gameobject.npc.HumanoidAnimationMaps
import demo.medieval_game.matrix.MedievalGameMatrixState
import engine.core.controllable.Controllable
import engine.core.controllable.Direction
import engine.core.loop.PredicateTimeEvent
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.getCenterPoint
import engine.core.window.Window
import engine.core.geometry.Point2D
import engine.core.render.AnimatedModel2D
import org.lwjgl.glfw.GLFW

class SimpleController2D(
    drawableComponent: AnimatedModel2D,
    private val params: SetOf2DParametersWithVelocity,
    private var absVelocityY: Float = 0f,
    private var absVelocityX: Float = 0f,
    private var modifier: Float = 20f,
    private var isControlledByUser: Boolean = false,
    private val onStrikingChange: (Boolean) -> Unit = {},
    private val onLooting: () -> Unit = {}
) : HumanoidAnimationController(
    drawableComponent,
    HumanoidAnimationMaps.getIdleMap(),
    HumanoidAnimationMaps.getStrikeMap(),
    HumanoidAnimationMaps.getWalkMap()
), Controllable {

    private val playStrikingAnimation = PredicateTimeEvent(
        timeLimit = 0.5f,
        predicate = { isStriking },
        action = {
            isStriking = false
            onStrikingChange.invoke(false)
        }
    )

    private val previousPosition = Point2D(0f, 0f)

    private var isLooting = false

    init {
        val center = params.getCenterPoint()
        previousPosition.x = center.x
        previousPosition.y = center.y
    }

    override fun input(window: Window) {
        if (!isControlledByUser) return

        if (isStriking) {
            params.velocityX = 0f
            params.velocityY = 0f
            return
        }

        if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            isStriking = true
            onStrikingChange.invoke(true)
        }

        params.velocityY = when {
            window.isKeyPressed(GLFW.GLFW_KEY_S) -> {
                direction = Direction.DOWN
                absVelocityY
            }
            window.isKeyPressed(GLFW.GLFW_KEY_W) -> {
                direction = Direction.UP
                -absVelocityY
            }
            else -> 0f
        }

        params.velocityX = when {
            window.isKeyPressed(GLFW.GLFW_KEY_D) -> {
                direction = Direction.RIGHT
                absVelocityX
            }
            window.isKeyPressed(GLFW.GLFW_KEY_A) -> {
                direction = Direction.LEFT
                -absVelocityX
            }
            else -> 0f
        }

        if (window.isKeyPressed(GLFW.GLFW_KEY_F)) {
            isLooting = true
            onLooting()
            isLooting = false
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        playStrikingAnimation.schedule(deltaTime)

        val center = params.getCenterPoint()
        val horizontalDiff = previousPosition.x - center.x
        val verticalDiff = previousPosition.y - center.y
        previousPosition.x = center.x
        previousPosition.y = center.y

        val horizontalMovement = params.velocityX * deltaTime * modifier
        val verticalMovement = params.velocityY * deltaTime * modifier

        params.x += horizontalMovement
        params.y += verticalMovement

        MedievalGameMatrixState.translateWorld(horizontalDiff, verticalDiff)
        processState()
    }

    private fun processState() {
        if (isStriking) {
            isWalking = false
            return
        }

        if ((params.velocityX != 0f || params.velocityY != 0f) && !isWalking) {
            isWalking = true
        }

        if (params.velocityY == 0f && params.velocityX == 0f) {
            isWalking = false
        }
    }
}