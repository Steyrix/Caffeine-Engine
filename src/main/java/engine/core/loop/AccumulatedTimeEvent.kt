package engine.core.loop

open class AccumulatedTimeEvent(
    protected val timeLimit: Float,
    protected val action: (Float) -> Unit,
    initialTime: Float = timeLimit
) {
    protected var accumulatedTime = initialTime

    open fun schedule(deltaTime: Float) {
        accumulatedTime += deltaTime

        if (accumulatedTime >= timeLimit) {
            accumulatedTime = 0f
            action.invoke(deltaTime)
        }
    }
}