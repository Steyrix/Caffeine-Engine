package engine.core.loop

open class AccumulatedTimeEvent(
        private val timeLimit: Float,
        private val action: (Float) -> Unit,
        initialTime: Float = timeLimit
) {
    private var accumulatedTime = initialTime

    open fun schedule(deltaTime: Float) {
        accumulatedTime += deltaTime

        if (accumulatedTime >= timeLimit) {
            accumulatedTime = 0f
            action.invoke(deltaTime)
        }
    }
}