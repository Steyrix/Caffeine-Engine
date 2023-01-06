package engine.feature.animation

class BasicAnimation(
        val name: String,
        private val animationId: Int,
        val usedLayerId: Int,
        private val framesCountX: Int,
        private val framesCountY: Int,
        internal var currentFrameX: Int,
        internal var currentFrameY: Int,
        private val timeLimit: Float
) {
    private var playFunction: ((Float, Int, Int, Int, Int) -> Unit)? = null

    private var lastPosX: Int = framesCountX
    private val lastPosY: Int = framesCountY

    private var firstPosX: Int = 1
    private val firstPosY: Int = 1

    private var accumulatedTime: Float = 0f

    init {
        addNewAnimToMap(this)
    }

    fun play(deltaTime: Float) {

        playFunction?.let {
            it.invoke(deltaTime, currentFrameX, currentFrameY, firstPosX, firstPosY)
            return
        }

        accumulatedTime += deltaTime
        if (accumulatedTime >= timeLimit) {
            accumulatedTime = 0f

            if (isMultiFramedByX()) {
                if (isLastFrameX()) {
                    currentFrameX = firstPosX

                    if (isMultiFramedByY()) {
                        if (isLastFrameY())
                            currentFrameY = firstPosY
                        else if (shouldInc())
                            currentFrameY++
                        else
                            currentFrameY --
                    }
                } else if (shouldInc())
                    currentFrameX++
                else currentFrameX--
            }
        }
    }

    private fun isLastFrameX() = if (shouldInc()) {
        currentFrameX + 1 > lastPosX
    } else {
        currentFrameX - 1 < lastPosX
    }

    private fun isLastFrameY() = if (shouldInc()) {
        currentFrameY + 1 > lastPosY
    } else {
        currentFrameY - 1 < lastPosY
    }

    private fun isMultiFramedByX() = framesCountX != 1

    private fun isMultiFramedByY() = framesCountY != 1

    private fun shouldInc() = lastPosX >= firstPosX

    fun setFirstPosX(posX: Int) {
        firstPosX = posX
    }

    fun setLastPosX(posX: Int) {
        lastPosX = posX
    }

    fun setCurrentFrameX(frame: Int) {
        currentFrameX = frame
    }

    fun setCurrentFrameY(frame: Int) {
        currentFrameY = frame
    }

    fun setPlayFunction(func: ((Float, Int, Int, Int, Int) -> Unit)?) {
        playFunction = func
    }

    companion object {
        private val map = HashMap<String, BasicAnimation>()

        fun addNewAnimToMap(a: BasicAnimation) {
            map[a.name] = a
        }

        fun animForName(animName: String): BasicAnimation? {
            return map[animName]
        }
    }
}