package engine.core.loop

class AccumulatedTimeEvent(
        private val timeLimit: Float,
        private val action: (Float) -> Unit
) {
    private var accumulatedTime = timeLimit

    fun schedule(deltaTime: Float) {
        accumulatedTime += deltaTime

        if (accumulatedTime >= timeLimit) {
            accumulatedTime = 0f
            action.invoke(deltaTime)
        }
    }
}