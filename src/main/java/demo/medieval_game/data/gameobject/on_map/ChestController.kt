package demo.medieval_game.data.gameobject.on_map

import demo.medieval_game.data.AnimationKey
import demo.medieval_game.interaction.ChestInteraction
import engine.core.controllable.AnimationController
import engine.core.entity.Entity
import engine.core.loop.PredicateTimeEvent
import engine.core.render.AnimatedModel2D
import engine.feature.interaction.Interaction

class ChestController(
    drawableComponent: AnimatedModel2D
) : AnimationController(drawableComponent), Entity {

    private val playOpeningAnimation = PredicateTimeEvent(
        timeLimit = 0.2f,
        predicate = { isOpening },
        action = {
            isOpening = false
            isClosed = false
            isClosing = false
        }
    )

    private val playClosingAnimation = PredicateTimeEvent(
        timeLimit = 0.2f,
        predicate = { isClosing },
        action = {
            isOpening = false
            isClosing = false
            isClosed = true
        }
    )

    private var isClosed = true
    private var isClosing = false
    private var isOpening = false

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        playOpeningAnimation.schedule(deltaTime)
        playClosingAnimation.schedule(deltaTime)
    }

    override fun getAnimationKey(): String {
        println("isClosed: $isClosed")
        println("isClosing: $isClosing")
        println("isOpening: $isOpening")
        return when {
            isClosed -> AnimationKey.CLOSED_CHEST
            isClosing -> AnimationKey.CLOSE
            isOpening -> AnimationKey.OPEN
            else -> AnimationKey.OPENED_CHEST
        }
    }

    override fun consumeInteraction(interaction: Interaction) {
        when(interaction) {
            is ChestInteraction.Open -> {
                if (isClosed) {
                    isClosed = false
                    isOpening = true
                }
            }
            is ChestInteraction.Close -> {
                if (!isClosed) {
                    isClosing = true
                }
            }
        }
    }
}