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
        timeLimit = 0.5f,
        predicate = { isOpening },
        action = {
            isClosed = false
            isOpening = false
            isClosing = false
        }
    )

    private val playClosingAnimation = PredicateTimeEvent(
        timeLimit = 0.6f,
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
        return when {
            isClosed -> AnimationKey.CLOSED_CHEST
            isClosing -> AnimationKey.CLOSE
            isOpening -> AnimationKey.OPEN
            else -> AnimationKey.OPENED_CHEST
        }
    }

    override fun consumeInteraction(interaction: Interaction) {
        when(interaction) {
            is ChestInteraction.OpenClose -> {
                if (isClosed) {
                    isClosed = false
                    isClosing = false
                    isOpening = true
                } else {
                    isClosing = true
                }
            }
        }
    }
}