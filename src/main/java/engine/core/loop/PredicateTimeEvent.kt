package engine.core.loop

class PredicateTimeEvent(
    private val timeLimit: Float,
    private val predicate: () -> Boolean,
    private val action: (Float) -> Unit
) {
    private var accumulatedTime = timeLimit

    fun schedule(deltaTime: Float) {
        if (predicate.invoke()) {
            accumulatedTime += deltaTime
        }

        if (accumulatedTime >= timeLimit) {
            accumulatedTime = 0f
            action.invoke(deltaTime)
        }
    }

    fun reset() {
        accumulatedTime = 0f
    }
}