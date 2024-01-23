package engine.core.loop

interface GameLoopTimeEvent {

    fun schedule(deltaTime: Float)
}