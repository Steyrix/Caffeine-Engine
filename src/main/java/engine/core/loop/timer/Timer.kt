package engine.core.loop.timer

private const val NANO_DIVIDER = 1_000_000_000.0

class Timer {

    private var lastTime: Double = getTime()

    fun getTime(): Double {
        return System.nanoTime() / NANO_DIVIDER
    }

    fun getElapsedTime(): Float {
        val time = getTime()
        val elapsedTime = time - lastTime
        lastTime = time
        return elapsedTime.toFloat()
    }

    fun getLastTime(): Double {
        return lastTime
    }
}