package engine.core.session

import org.joml.Matrix4f

interface SessionPresets

data class SimpleGamePresets(
        val screenWidth: Float,
        val screenHeight: Float,
        val renderProjection: Matrix4f
) : SessionPresets