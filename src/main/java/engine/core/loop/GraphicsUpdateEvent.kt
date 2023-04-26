package engine.core.loop

import engine.core.render.render2D.Drawable2D

class GraphicsUpdateEvent(
        private val graphicalComponent: Drawable2D,
        timeLimit: Float,
        action: (Float) -> Unit,
        initialTime: Float
) : AccumulatedTimeEvent(timeLimit, action, initialTime)