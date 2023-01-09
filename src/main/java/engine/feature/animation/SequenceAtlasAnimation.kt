package engine.feature.animation

import engine.core.shader.Shader

class SequenceAtlasAnimation(
        override val name: String,
        private val frames: List<FrameParameters>,
        initialIndex: Int = 0,
        private val timeLimit: Float
) : Animation {

    private var accumulatedTime: Float = 0f
    private var currIndex = 0

    var currentFrame: FrameParameters? = null

    init {
        if (frames.isNotEmpty()) {
            currentFrame = if (initialIndex < frames.size) {
                frames[initialIndex]
            } else {
                frames.first()
            }
        }

        currIndex = initialIndex
    }

    override fun play(deltaTime: Float) {
        if (currentFrame == null) return

        accumulatedTime += deltaTime

        if (accumulatedTime >= timeLimit) {
            accumulatedTime = 0f

            if (currIndex + 1 < frames.size) {
                currIndex++
            } else {
                currIndex = 0
            }


            currentFrame = frames[currIndex]
        }
    }

    override fun setUniforms(shader: Shader) {
        TODO("Not yet implemented")
    }
}