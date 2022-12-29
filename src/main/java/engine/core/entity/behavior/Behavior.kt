package engine.core.entity.behavior

import engine.core.update.SetOfParameters

interface Behavior {
    fun execute(
            deltaTime: Float,
            params: SetOfParameters
    )
}