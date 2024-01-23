package engine.core.loop

open class AccumulatedTimeEvent(
    protected val timeLimit: Float,
    protected val action: (Float) -> Unit,
    initialTime: Float = timeLimit
) : GameLoopTimeEvent {
    protected var accumulatedTime = initialTime

    override fun schedule(deltaTime: Float) {
        accumulatedTime += deltaTime

        if (accumulatedTime >= timeLimit) {
            accumulatedTime = 0f
            action.invoke(deltaTime)
        }
    }
}