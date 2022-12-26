package engine.core.entity.behavior

import engine.core.update.SetOfParameters

sealed class Behavior

data class SimpleBehavior(
        val parameterChanging: (SetOfParameters) -> Unit,
) : Behavior()

data class LoopedBehavior(
        val isLoop: Boolean = false,
        val loopCondition: (SetOfParameters) -> Boolean,
        val parameterChanging: (SetOfParameters) -> Unit,
) : Behavior()