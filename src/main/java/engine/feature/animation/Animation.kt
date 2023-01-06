package engine.feature.animation

interface Animation {
    val name: String

    fun play(deltaTime: Float)
}