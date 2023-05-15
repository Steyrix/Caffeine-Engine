package engine.core.loop

class SingleTimeEvent(
        timeLimit: Float,
        action: (Float) -> Unit,
        initialTime: Float = timeLimit
) : AccumulatedTimeEvent(timeLimit, action, initialTime) {

    var isFinished = false
        private set

    override fun schedule(deltaTime: Float) {
        if (!isFinished) {
            accumulatedTime += deltaTime

            if (accumulatedTime >= timeLimit) {
                accumulatedTime = 0f
                action.invoke(deltaTime)
                isFinished = true
            }
        }
    }
}