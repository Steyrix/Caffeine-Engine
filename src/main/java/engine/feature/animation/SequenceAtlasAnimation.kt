package engine.feature.animation

import engine.core.shader.Shader

// TODO: ability to reset animations
class SequenceAtlasAnimation(
    override val name: String,
    override val isCycled: Boolean = true,
    private val frames: List<FrameParameters>,
    private val initialIndex: Int = 0,
    private val timeLimit: Float
) : Animation {

    private var accumulatedTime: Float = 0f
    private var currIndex = 0

    private var currentFrame: FrameParameters? = null

    init {
        reset()
    }

    override fun play(deltaTime: Float) {
        if (currentFrame == null) return

        accumulatedTime += deltaTime

        if (accumulatedTime >= timeLimit) {
            accumulatedTime = 0f

            if (currIndex + 1 < frames.size) {
                currIndex++
            } else {
                if (isCycled) {
                    currIndex = 0
                    // TODO: This causes frame glitch of chests
                }
            }

            currentFrame = frames[currIndex]
        }
    }

    override fun setUniforms(shader: Shader) {
        currentFrame?.let {
            shader.setUniform(Shader.VAR_KEY_X_OFFSET, it.xOffset)
            shader.setUniform(Shader.VAR_KEY_FRAME_X, it.frameNumberX)
            shader.setUniform(Shader.VAR_KEY_Y_OFFSET, it.yOffset)
            shader.setUniform(Shader.VAR_KEY_FRAME_Y, it.frameNumberY)
        }
    }

    override fun toString(): String {
        return "Animation $name: currentFrame is ${currentFrame.toString()}"
    }

    override fun copy(): SequenceAtlasAnimation {
        return SequenceAtlasAnimation(
            name,
            isCycled,
            frames = frames.map { it.copy() },
            initialIndex = 0,
            timeLimit = timeLimit
        )
    }

    override fun reset() {
        if (frames.isNotEmpty()) {
            currentFrame = if (initialIndex < frames.size) {
                frames[initialIndex]
            } else {
                frames.first()
            }
        }

        currIndex = initialIndex
    }
}