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
            super.schedule(deltaTime)
            isFinished = true
        }
    }
}