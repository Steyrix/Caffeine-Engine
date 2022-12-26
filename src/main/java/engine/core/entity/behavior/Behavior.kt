package engine.core.entity.behavior

import engine.core.update.SetOfParameters

sealed class Behavior

data class SimpleBehavior(
        val parameterChanging: (Float, SetOfParameters) -> Unit,
) : Behavior()

data class LoopedBehavior(
        val loopCondition: (SetOfParameters) -> Boolean,
        val parameterChanging: (Float, SetOfParameters) -> Unit,
) : Behavior()