package engine.core.loop

import engine.core.loop.timer.Timer
import engine.core.window.Window

class FixedStepGameLoop(
    private val targetWindow: Window,
    private val timer: Timer,
    private val targetFps: Int = 60,
    private val targetUpdateSecs: Int = 30
) : GameLoop {

    override fun loop(
        inputFunc: () -> Unit,
        renderFunc: () -> Unit,
        updateFunc: (Float) -> Unit
    ) {
        var elapsedTime: Float
        var accumulator = 0f

        val interval = 1f / targetUpdateSecs
        var isRunning = true

        while (isRunning && !targetWindow.shouldClose()) {
            elapsedTime = timer.getElapsedTime()
            accumulator += elapsedTime

            inputFunc()

            while (accumulator >= interval) {
                updateFunc(interval)
                accumulator -= interval
            }

            renderFunc()

            if (!targetWindow.isVsyncEnabled) {
                sync()
            }
        }
    }

    private fun sync() {
        val loopSlot = 1f / targetFps
        val endTime = timer.getLastTime() + loopSlot

        while (timer.getTime() <= endTime) {
            try {
                Thread.sleep(1)
            } catch (e: InterruptedException) {

            }
        }
    }
}