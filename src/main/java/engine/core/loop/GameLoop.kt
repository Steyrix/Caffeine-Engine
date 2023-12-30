package engine.core.loop

interface GameLoop {
    fun loop(
        inputFunc: () -> Unit,
        renderFunc: () -> Unit,
        updateFunc: (Float) -> Unit,
    )
}